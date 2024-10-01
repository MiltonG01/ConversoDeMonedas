package com.trabajo.conversorDeMonedas;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConversorMoneda {

    public static void main(String[] args) throws IOException, InterruptedException {


        while (true) {
            System.out.println("*****************Conversor de monedas***************** ");
            System.out.println("Seleccione una opción a convertir ");
            System.out.println("1) Dólar \t\t==> Peso Argentino");
            System.out.println("2) Peso Argentino \t==> Dólar");
            System.out.println("3) Dólar \t\t==> Real Brasileño ");
            System.out.println("4) Real Brasileño \t==> Dólar");
            System.out.println("5) Dólar \t\t==> Peso Colombiano");
            System.out.println("6) Peso Colombiano \t==> Dólar");
            System.out.println("7) Salir ");

            System.out.println("*****************Seleccione la opcion*****************");
            Scanner escritura = new Scanner(System.in);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try {




                    String busqueda = "";
                    String monedaDestino = "";

                    // Seleccionar valor según la opción del usuario
                    int opcion = escritura.nextInt();
                    switch (opcion) {
                        case 1:
                            busqueda = "USD";
                            monedaDestino = "ARS";  // Dólar a Peso Argentino
                            break;
                        case 2:
                            busqueda = "ARS";
                            monedaDestino = "USD";  // Peso Argentino a Dólar
                            break;
                        case 3:
                            busqueda = "USD";
                            monedaDestino = "BRL";  // Dólar a Real Brasileño
                            break;
                        case 4:
                            busqueda = "BRL";
                            monedaDestino = "USD";  // Real Brasileño a Dólar
                            break;
                        case 5:
                            busqueda = "USD";
                            monedaDestino = "COP";  // Dólar a Peso Colombiano
                            break;
                        case 6:
                            busqueda = "COP";
                            monedaDestino = "USD";  // Peso Colombiano a Dólar
                            break;
                        case 7:
                                System.out.println("Saliendo del programa ");
                                System.exit(1);
                        default:
                            System.out.println("Opción inválida.");
                            return;
                    }

                    // Solicitar al usuario que ingrese el valor que desea convertir
                    System.out.println("Ingrese el valor que desea convertir de " + busqueda + " a " + monedaDestino + ": ");
                    double montoAConvertir = escritura.nextDouble();

                    // Requerir la información del API con la moneda base seleccionada
                    String direccion = "https://v6.exchangerate-api.com/v6/a146b2f1b27f30228e1da6c3/latest/" + busqueda;

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(direccion))
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    String json = response.body();
                    //System.out.println("Respuesta JSON recibida: \n" + json);

                    // convertir el JSON a un objeto JsonObject
                    JsonObject jsonobj = JsonParser.parseString(json).getAsJsonObject();

                    // Verificar si la clave "conversion_rates" existe
                    if (jsonobj.has("conversion_rates")) {
                        JsonObject conversionRates = jsonobj.getAsJsonObject("conversion_rates");

                        // Buscar el valor de la moneda de destino
                        if (conversionRates.has(monedaDestino)) {
                            double tasaConversion = conversionRates.get(monedaDestino).getAsDouble();
                            double resultadoConvertido = montoAConvertir * tasaConversion;  // Calcular el monto convertido

                            System.out.println("La tasa de conversión de " + busqueda + " a " + monedaDestino + " es: " + tasaConversion);
                            System.out.println("El valor convertido es: " + resultadoConvertido + " " + monedaDestino);



                        } else {
                            System.out.println("No se encontró la tasa de conversión para la moneda: " + monedaDestino);
                        }
                    } else {
                        System.out.println("No se encontraron tasas de conversión en el JSON.");
                    }



            } catch(Exception e){
                System.out.println("Ocurrio un error: ");
                System.out.println(e.getMessage());
            }

        }


    }

}
