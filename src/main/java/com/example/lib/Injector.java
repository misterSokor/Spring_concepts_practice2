
package com.example.lib;

import com.example.dao.ProductDao;
import com.example.dao.impl.ProductDaoImpl;
import com.example.lib.InjectConstructor;
import com.example.service.ProductService;
import com.example.service.impl.ProductServiceImpl;
import com.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Injector {
    private static final Map<Class<?>, Class<?>> interfaceToImplementation =
            new HashMap<>();

    static {
        interfaceToImplementation.put(ProductService.class,
                ProductServiceImpl.class);
        interfaceToImplementation.put(ProductDao.class, ProductDaoImpl.class);
    }


    public static <T> T getInstance(Class<T> clazz) {
        try {
            clazz = (Class<T>) getImplementationClass(clazz);
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(InjectConstructor.class)) {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    Object[] objectsOfParameters = new Object[parameterTypes.length];
                    for (int i = 0; i < objectsOfParameters.length; i++) {
                        if (parameterTypes[i].equals(SessionFactory.class)) {
                            objectsOfParameters[i] = HibernateUtil.getSessionFactory();
                        } else {
                            objectsOfParameters[i] = getInstance(parameterTypes[i]);
                        }
                    }
                    constructor.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    T instance = (T) constructor.newInstance(objectsOfParameters);
                    injectFields(instance);
                    return instance;
                }
            }
            T instance = clazz.getDeclaredConstructor().newInstance();
            injectFields(instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Can't create an instance of " + clazz.getName(), e);
        }
    }

    private static void injectFields(Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try {
                    Object fieldInstance = getInstance(field.getType());
                    field.set(instance, fieldInstance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject field: " + field.getName(), e);
                }
            }
        }
    }

    private static Class<?> getImplementationClass(Class<?> clazz) {
        return interfaceToImplementation.getOrDefault(clazz, clazz);
    }

}



/*

package com.example.lib;

import com.example.lib.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Injector {
    private static final Map<String, Injector> injectors = new HashMap<>();
    private final Map<Class<?>, Object> instanceOfClasses = new HashMap<>();
    private final List<Class<?>> classes = new ArrayList<>();

    private Injector(String mainPackageName) {
        try {
            classes.addAll(getClasses(mainPackageName));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Can't get information about all classes", e);
        }
    }

    public static Injector getInjectorsInstance(String mainPackageName) {
        if (injectors.containsKey(mainPackageName)) {
            return injectors.get(mainPackageName);
        }
        Injector injector = new Injector(mainPackageName);
        injectors.put(mainPackageName, injector);
        return injector;
    }

    public Object getObjectsInstance(Class<?> certainInterface) {
        Object newInstanceOfClass = null;
        Class<?> clazz = findClassExtendingInterface(certainInterface);
        Object instanceOfCurrentClass = createInstance(clazz);
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (isFieldInitialized(field, instanceOfCurrentClass)) {
                continue;
            }
            if (field.getDeclaredAnnotation(Inject.class) != null) {
                Object classToInject = getObjectsInstance(field.getType());
                newInstanceOfClass = getNewInstance(clazz);
                setValueToField(field, newInstanceOfClass, classToInject);
            } else {
                throw new RuntimeException("Class " + field.getName() + " in class "
                                           + clazz.getName() + " hasn't annotation Inject");
            }
        }
        if (newInstanceOfClass == null) {
            return getNewInstance(clazz);
        }
        return newInstanceOfClass;
    }

    private Class<?> findClassExtendingInterface(Class<?> certainInterface) {
        for (Class<?> clazz : classes) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> singleInterface : interfaces) {
                if (singleInterface.equals(certainInterface)
                    && (clazz.isAnnotationPresent(Service.class)
                        || clazz.isAnnotationPresent(Dao.class))) {
                    return clazz;
                }
            }
        }
        throw new RuntimeException("Can't find class which implements "
                                   + certainInterface.getName()
                                   + " interface and has valid annotation (Dao or Service)");
    }

    private Object getNewInstance(Class<?> certainClass) {
        if (instanceOfClasses.containsKey(certainClass)) {
            return instanceOfClasses.get(certainClass);
        }
        Object newInstance = createInstance(certainClass);
        instanceOfClasses.put(certainClass, newInstance);
        return newInstance;
    }

    private boolean isFieldInitialized(Field field, Object instance) {
        field.setAccessible(true);
        try {
            return field.get(instance) != null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't getByMoviesId access to field");
        }
    }

    private Object createInstance(Class<?> clazz) {
        Object newInstance;
        try {
            Constructor<?> classConstructor = clazz.getConstructor();
            newInstance = classConstructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can't create object of the class", e);
        }
        return newInstance;
    }

    private void setValueToField(Field field, Object instanceOfClass, Object classToInject) {
        try {
            field.setAccessible(true);
            field.set(instanceOfClass, classToInject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't set value to field ", e);
        }
    }
    /**
     * Scans all classes accessible from the context class loader which
     * belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException if the class cannot be located
     * @throws IOException            if I/O errors occur
     */
/*
    private static List<Class<?>> getClasses(String packageName)
            throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            throw new RuntimeException("Class loader is null");
        }
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }
    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException if the class cannot be located
     */
/*
    private static List<Class<?>> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (file.getName().contains(".")) {
                        throw new RuntimeException("File name shouldn't consist point.");
                    }
                    classes.addAll(findClasses(file, packageName + "."
                                                     + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.'
                                              + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }
*/
