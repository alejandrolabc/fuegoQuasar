
# PruebaMeliFuegoQuasar

## [](#autor)Autor

-   **Luis Alejandro Balaguera**

## [](#versión)Versión

1.0

## [](#descripción)Descripción

La finalidad de este proyecto, es la de generar un programa que permita determinar por medio de trilateracion la posición de un mensaje, asi como también unificar fragmentos del mensaje, para devolverlo completo.

## [](#ambiente)Ambiente

A continuación, se describe el ambiente en que se diseñó esta versión del proyecto;


<table>
<tr><th>Descripción</th><th>Nombre</th><th>Versión</th></tr>
<tr><td>Sistema Operativo</td><td>Windows</td><td>10 x64</td></tr>
<tr><td>Gestor de dependencias</td><td>Maven</td><td>3.8.1</td></tr>
<tr><td>Framework</td><td>SpringBoot</td><td>2.3.4 RELEASE</td></tr>
<tr><td>IDE</td><td>Intellij IDEA</td><td>Community 2021.1</td></tr>
<tr><td>JDK</td><td>Java</td><td>11 x64</td></tr>
</table>

# [](#procesos)Procesos

## [](#instalación)Instalación

El proyecto se encuentra en un repositorio GitLab, para poderlo descargar se deben seguir los siguientes pasos:

1.  Tener instalado un cliente GIT
2.  Descargar el repositorio al local con la URL proporcionada
3.  Ubicar el branch master
4.  Abrir el proyecto en el IDE de preferencia

## [](#configuracion)Configuracion

Para que el microservicio funcione correctamente, se debe validar que en el archivo de propiedades se le tenga valor a las propiedades de kenobi.longitude, kenobi.latitude, skywalker.longitude, skywalker.latitude, sato.longitude y sato.latitude, las cuales contienen la posición de los satelites que se usaran para determinar luego la posición.

## [](#compilación)Compilación

Una vez se descargue el proyecto, se deberan descargar las dependencias correspondientes y compilar el proyecto con el comando 
```mvn clean install```

## [](#despliegue)Despliegue

Para poder realizar el despliegue del microservicio se debe hacer lo siguiente

```
 1. Compilar el proyecto corriendo el comando mvn clean install
 2. Buscar la clase principal del proyecto y darle click derecho Run.
```

Estos pasos estan realizados con el IDE Intellij

## [](#funcionamiento)Funcionamiento

El funcionamiento del programa se divide en 3 paths o servicios, los cuales son:
```
	- /topsecret/
	- /topsecret_split/{satellite_name}
```
El primer servicio se consume por medio del método POST, mientras que el segundo, se puede consumir tanto por el método GET como por el método POST.

para el correcto funcionamiento del primer servicio se le debe enviar la data en formato json con la información de los 3 satelites, como por ejemplo:
```
{
  "satellites": [
    {
      "name": "kenobi",
      "distance": 100,
      "message": ["este","","","mensaje",""]
    },
    {
      "name": "skywalker",
      "distance": 115.5,
      "message": ["","es","","","secreto"]
    },
    {
      "name": "sato",
      "distance": 142.7,
      "message": ["este","","un","",""]
    }
  ]
}
```
Para el segundo servicio si se va a consumir por método POST, se le debe pasar el nombre del satelite en el path asi como tambien envíarle una estructura json como la siguiente: 
```
{
"distance": 115.5,
"message": ["este","","un","",""]
}
```
El json contiene los datos de un satelite, el servicio respondera satisfactoriamente luego de que se le haya enviado la información de los 3 satelites, es decir se consume el servicio con el nombre y la información del primer satelite, luego se le cambia en el path el nombre por el segundo satelite, en el json se pone la información de este segundo satelite, y por último se hace lo mismo pero con el tercer satelite; el servicio internamente va almacenando en memoria la información de los 3 satelites, y una vez la tenga ahi si puede continuar con el proceso de determinar la posición y el mensaje.

Para consumir el servicio por método GET se le debe enviar el nombre del servicio en el path, y luego como parametros la distancia y el mensaje:
```
http://localhost:8080/topsecret_split/sato?distance=142.7&message=este, ,un, , 
```
en el mensaje se le envia los fragmentos del mensaje separados por coma, en caso de no tener una palabra se deja un espacio y se agrega otra coma. Este método GET funciona igual que el POST, necesita que sea consumido 3 veces con la información de los 3 satelites, para poder retornar satisfactoriamente la posición y el mensaje.
