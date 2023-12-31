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

## 3. Setear la action bar como barra de acción de la actividad
Esto es muy sencillo. Simplemente ejecutamos esto en el onCreate() de la Actividad:
```kotlin
  //Seteamos la toolbar como Action Bar
  setSupportActionBar(binding.topAppBar)
```
Podríamos recuperar referencias a la barra con el getter correspondiente:
```kotlin
  val miActionBar = getSupportActionBar()
```
Si ejecutamos la aplicación ahora, veremos nuestra ActionBar con el título de la app, pero sin opciones. Al convertirse en ActionBar tenemos que inflar el menú desde la actividad.
Toda la gestión de este menú a partir de ahora, seteada como ActionBar, la haremos siguiendo hooks de la API de Android

## 4. Inflar la Action Bar
Para inflar la toolbar seteada como ActionBar, usaremos el hook onCreateOptionsMenu:
```kotlin
    //Si la seteamos como actionBar, tenemos que inflar el menú con este hook
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }
```
Tras esto las opciones del menú volverán a mostrarse.


## 5. Gestionar nuestra barra con un gráfico de navegación (Navigation UI)
El Navigation Component nos provee una clase NavigationUI que nos permite gestionar automáticamente la navegación, tal y como está definida
en el gráfico de navegación, desde nuestros menús. 

Nos permite:
- Mostrar el label de los fragmentos del gráfico de navegación como título de cada ventana.
- Gestionar automáticamente la visibilidad del botón arriba del botón "Up" <img src="https://developer.android.com/static/images/guide/navigation/up-button.png" style="height:1.20em"> de la parte inferior izquierda, para que se muestre en todos los fragmentos no raíz.
- Vincular nuestros items de menú y los destinos de navegación mediante su id.


Si tenemos nuestra ToolBar seteada como actionBar, tenemos que seguir [estos pasos](https://developer.android.com/guide/navigation/integrations/ui#add_a_navigation_drawer).
No debemos olvidarnos de sobreescribir este método:
```kotlin
override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
}
```
Si fuese una ToolBar normal (sin haber ejecutado```setSupporActionBar()```), seguiremos [estos otros pasos](https://developer.android.com/guide/navigation/integrations/ui#create_a_toolbar)

En ambos casos, tendremos que sobreescribir de algún modo el comportamiento al hacer click en los items del menú. Con la ActionBar lo haremos
en el hook ```onOptionsItemSelected()``` como se indica [aquí](https://developer.android.com/guide/navigation/integrations/ui#Tie-navdrawer):

```kotlin
override fun onOptionsItemSelected(item: MenuItem): Boolean {
   val navController = findNavController(R.id.nav_host_fragment)
   return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
}
```


# Añadir un menú lateral deslizante

Podemos añadir otro menú llamado Navigation Drawer. No hay buena documentación al respecto, pero no es complicado.
El menú se abrirá con un icono de navegación configurado en la ToolBar desde los destinos de nivel superior (en los destinos internos, aparece en vez de ese icono el botón Up mencionado antes).

Seguiremos [la guía de material correspondiente](https://github.com/material-components/material-components-android/blob/master/docs/components/NavigationDrawer.md#navigation-drawer) y el [apartado correspondiente de la guía Navigation Component](https://developer.android.com/guide/navigation/navigation-ui?hl=es-419#add_a_navigation_drawer)

## 1. Cambios en el layout
 - Añadiremos un icono de navegación, el que queramos, a nuestra ToolBar. Simplemente con el atributo ```app:navigationIcon="@drawable/mi_icono"```
 - Debemos envolver todo el layout en el que tenemos nuestra ToolBar con otro layout contenedor: DrawerLayout. Y añadir al final un NavigationView. 
   
   ```xml
   <androidx.drawerlayout.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       xmlns:tools="http://schemas.android.com/tools"
       android:id="@+id/drawerLayout"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       tools:openDrawer="start">
    
      <!-- Aquí todo lo que teníamos. La barra, el nav_host... -->
   
      <com.google.android.material.navigation.NavigationView
              android:id="@+id/nav_view"
              android:layout_width="wrap_content"
              android:layout_height="match_parent"
              android:layout_gravity="start"
              app:menu="@menu/navigation_menu"
              app:headerLayout="@layout/navdrawer_header"/>   
      
   </androidx.drawerlayout.widget.DrawerLayout>
   ```
   Dos cosas a destacar:
 - El NavigationView apunta a otro menu.xml distinto. En este menú, cada opción se muestra siempre con icono y texto, y además, se genera una línea separada entre elementos group con ids distintos. Echad un ojo al menú enlazado.  
 - En el NavigationView se hace referencia a un layout que hará de cabecera. No es obligatorio, pero
   ahí podemos añadir el título de nuestra App, el icono, etc... para darle un toque distintivo.

Tras ello, los cambios en el código del MainActivity son mínimos. Simplemente hay que tener en cuenta el Navigation Drawer al setear el appBarConfiguration.
Se muestran en la imagen los cambios. A la derecha el código nuevo y a la izquierda el original (sólo con la ToolBar):

![img.png](img.png)

Si los ids del menú del Navigation drawer coinciden con los destinos del gráfico de navegación, el menú ya se gestionará automáticamente por dicho gráfico de navegación.



Para finalizar, hay otros tipos de menús que podemos usar. Uno muy común es el [BottomAppBar](https://m3.material.io/components/bottom-app-bar/overview). Y también
podemos generar [menús conextuales desplegables](https://m3.material.io/components/bottom-app-bar/overview) que se muestran al hacer click o dejar pulsada una vista.







 


