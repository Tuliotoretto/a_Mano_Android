
# a Mano Andorid.

Proyecto final del Diplomado de Desarrollo de Aplicaciones para dispositivos móviles. 

## Objetivo

El objetivo de esta aplicación es poder administrar tus deudas con amigos o familiares, de una forma facil. 

## Logo

El logo son las manos de dos personas dandose la mano. Haciendo referencia a que "cerraron el trato" y no hay deudas. en pocas palabras, estan "a mano"

## Instrucciones

- Inicia sesion con tu telefono celular
- crea grupos con tus amigos o familiares
- agrega gastos en cada uno de los grupos y sigue tus cuentas

## Dispositivo

Se eligió como dispositivo unicamente celulares en orientación vertical, ya que el objetivo de esta aplicacion es tener a la mano tus cuentas. para verificarlas rapidamente.

la minuma SDK de la aplicacion es la 26. Versión recomendada por google el 2022.

## Credenciales

usuario de prueba 1:\
numero: +1 650-555-1111 \
codigo: 111111

usuario de prueba w:\
numero: +1 1231231234 \
codigo: 111111

## Dependencias

    // firebase
    implementation platform('com.google.firebase:firebase-bom:31.2.0')
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.firebaseui:firebase-ui-auth:8.0.2'
    implementation 'androidx.preference:preference:1.2.0'

    // Circle Image View
    implementation 'de.hdodenhof:circleimageview:3.1.0'
