# Toolbar como ActionBar
Las guías de android sobre [la barra de aplicación](https://developer.android.com/training/appbar/setting-up?hl=es-419) nos explican que desde Android 3.0, 
todas las actividades con el tema predeterminado tienen una ActionBar por defecto. Esto no es así al menos desde la versión de Android Studio Giraffe que estamos usando, ya que siempre
empezamos con theme que termina en NoActionBar. 

Esto vino precisamente por lo explicado en dicha guía: a medida que Android iba evolucinando en su versión se fueron añadiendo cada vez más funcionalidades a la barra de acción nativa, lo que en definitiva
provocó que la barra de acción nativa se vea distinta dependiendo de la versión de Android del dispositivo. Por ese motivo, recomienda usar un theme NoActionBar y 
usar en su lugar una ToolBar totalmente personalizada y controlada por nosotros. 

En este proyecto veremos cómo implementar la ToolBar en el layout, como setearla como ActionBar, cómo inflarla con un menú, y cómo cómo conectarlo de forma 
automática al gráfico de navegación del Navigation Component. 


## 1. Configurar el layout
 - Seguimos esta estructura básica en nuestro layout para la actividad principal:
   https://github.com/material-components/material-components-android/blob/master/docs/components/TopAppBar.md#small-top-app-bar-example

    En nuestro caso, no usamos una NestedScrollView, sino un fragment que hará de NavHostFragment para ver cómo se conecta. 

    Es muy importante incluir el atributo ```app:layout_behavior``` en el fragment, para que su contenido no se monte debajo de la barra. 
 
    Esto ocurre por usar un CoordinatorLayout, que por otro lado, nos puede dar
    comportamientos interesantes al conectar los scrolls de la barra con el de otros contenidos.

 - Completamos el diseño de la barra siguiendo [el punto 4 de esta otra guía](https://developer.android.com/develop/ui/views/components/appbar/setting-up#add-toolbar) que nos muestra:
    ```xml
    <androidx.appcompat.widget.Toolbar
        android:id="@+id/my_toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        <!-- Cosas a añadir -->
        android:background="?attr/colorPrimary"
        android:elevation="4dp"
        android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>
    ```
   Theme es el theme principal de la barra, y popUpTheme es el theme del menú emergente (el de los 3 puntos).
 - Fijaros que en este caso, nuestro navHostFragment no es un FragmentContainerView, sino un fragment a secas. Esto es porque si no hay funciones de la guía que rompen la app.

Finalmente nuestro layout queda de este modo:
```xml
    <?xml version="1.0" encoding="utf-8"?>
    <androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
    
        <com.google.android.material.appbar.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fitsSystemWindows="true">
            <!-- Aquí podríamos meter imágenes de fondo u otras cosas para "dopar" nuestra ToolBar, que es 
             lo que en la primera guía se llama una Collapsing ActionBar -->
            <com.google.android.material.appbar.MaterialToolbar
                android:id="@+id/topAppBar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="?attr/colorPrimary"
                android:elevation="4dp"
                android:minHeight="?attr/actionBarSize"
                android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
                app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />
    
        </com.google.android.material.appbar.AppBarLayout>
    
        <!-- Muy importante el app:layout_behaviour,
        para que no se monte el contenido del fragmento bajo la barra -->
        <fragment
            android:id="@+id/nav_host"
            android:name="androidx.navigation.fragment.NavHostFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:defaultNavHost="true"
            app:navGraph="@navigation/nav_graph"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"
            />
    
    </androidx.coordinatorlayout.widget.CoordinatorLayout>
```
Si ejecutamos la app ahora, veremos nuestra toolbar, aunque sin poder interacturar con sus items  (si los hemos cargado desde un menú) ya que no les hemos dado
aún comportamiento. 

Por otro lado, veréis que no se muestra el título de la aplicación en la barra. Esto es porque esa toolbar aún no es la ActionBar de la aplicación (de la actividad en este caso).
Vamos a ello:

## 2. Crear un menú de opciones
Podemos áñadir las opciones al menú programáticamente con el hook ```onCreateOptionsMenu():Boolean```, o bien podemos definirlo en un recurso de tipo menú
desde Android Studio. Creo que es mucho más sencilla la segunda opción. Podéis seguir [esta guía de Android sobre los recursos de menú.](https://developer.android.com/develop/ui/views/components/menus#xml)

Una vez creado, podemos asociar ya nuestro menú a nuestra ToolBar con ```app:menu="@menu/top_menu"```, pero si la seteamos como ActionBar, cosa que haremos a continuación, no servirá para nada. 
Esto está pensado más para otros menús auxiliares, como la BottomAppBar o el Navigation Drawer. 

## 2. Setear la action bar como barra de acción de la actividad
Esto es muy sencillo. Simplemente ejecutamos esto en el onCreate() de la Actividad:
```kotlin
  //Seteamos la toolbar como Action Bar
  setSupportActionBar(binding.topAppBar)
```
Si ejecutamos la aplicación ahora, veremos nuestra ActionBar coj el título de la app, pero sin opciones. Tenemos que inflarlo desde la actividad. 


