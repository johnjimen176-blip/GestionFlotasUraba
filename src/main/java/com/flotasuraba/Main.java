package com.flotasuraba;

import com.flotasuraba.entity.Conductor;
import com.flotasuraba.entity.Ruta;
import com.flotasuraba.entity.Vehiculo;
import com.flotasuraba.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("    UNIREMINGTON - SISTEMA DE GESTION DE FLOTAS INTERMUNICIPALES URABA    ");
        System.out.println("                     TERMINAL DE TRANSPORTES CHIGORODO                          ");
        System.out.println("================================================================================\n");

        Long vehiculoId = null;
        Long rutaId = null;
        Long conductorId = null;

        // 1. OPERACION CREATE (Guardar 1 vehiculo, 1 ruta y 1 conductor)
        System.out.println(">>> [FASE 1 - CREATE] Guardando registros iniciales en la base de datos flotas_uraba...");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Verifica y limpia registros previos con la misma placa o cédula para permitir re-ejecuciones
            Vehiculo vPrevio = session.createQuery("from Vehiculo where placa = :placa", Vehiculo.class)
                    .setParameter("placa", "WLF-789")
                    .uniqueResult();
            if (vPrevio != null) {
                session.remove(vPrevio);
            }

            Conductor cPrevio = session.createQuery("from Conductor where cedula = :cedula", Conductor.class)
                    .setParameter("cedula", "1038765432")
                    .uniqueResult();
            if (cPrevio != null) {
                session.remove(cPrevio);
            }

            // Ejecuta los DELETE pendientes en la BD antes de los INSERT
            session.flush();

            // Creando Vehiculo para la ruta Chigorodo - Apartado
            Vehiculo vehiculo = new Vehiculo("WLF-789", 19, "Microbús", "ACTIVO");
            session.persist(vehiculo);

            // Creando Ruta intermunicipal Chigorodo -> Apartado
            Ruta ruta = new Ruta("Chigorodó", "Apartadó", 12000.0, 16.5);
            session.persist(ruta);

            // Creando Conductor asignado
            Conductor conductor = new Conductor("Carlos Mario Henao", "1038765432", "C2-789456", "3127894561");
            session.persist(conductor);

            tx.commit();

            vehiculoId = vehiculo.getId();
            rutaId = ruta.getId();
            conductorId = conductor.getId();

            System.out.println("   [OK] Vehículo guardado con ID: " + vehiculoId + " (Placa: " + vehiculo.getPlaca() + ")");
            System.out.println("   [OK] Ruta guardada con ID: " + rutaId + " (" + ruta.getOrigen() + " -> " + ruta.getDestino() + ")");
            System.out.println("   [OK] Conductor guardado con ID: " + conductorId + " (" + conductor.getNombre() + ")");
        } catch (Exception e) {
            System.err.println(">>> Error durante la fase CREATE: " + e.getMessage());
            e.printStackTrace();
            HibernateUtil.shutdown();
            return;
        }

        // 2. OPERACION READ (Consultar y listar los registros guardados)
        System.out.println("\n>>> [FASE 2 - READ] Consultando registros desde la base de datos...");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Vehiculo v = session.get(Vehiculo.class, vehiculoId);
            Ruta r = session.get(Ruta.class, rutaId);
            Conductor c = session.get(Conductor.class, conductorId);

            System.out.println("   -> Consulta individual:");
            System.out.println("      " + v);
            System.out.println("      " + r);
            System.out.println("      " + c);

            System.out.println("\n   -> Listado completo de vehículos registrados:");
            List<Vehiculo> listaVehiculos = session.createQuery("from Vehiculo", Vehiculo.class).list();
            for (Vehiculo item : listaVehiculos) {
                System.out.printf("      [Vehículo #%d] Placa: %s | Tipo: %s | Capacidad: %d pasajeros | Estado: %s%n",
                        item.getId(), item.getPlaca(), item.getTipo(), item.getCapacidad(), item.getEstado());
            }

            System.out.println("\n   -> Listado completo de rutas registradas:");
            List<Ruta> listaRutas = session.createQuery("from Ruta", Ruta.class).list();
            for (Ruta item : listaRutas) {
                System.out.printf("      [Ruta #%d] %s -> %s | Tarifa: $%,.2f COP | Distancia: %.1f km%n",
                        item.getId(), item.getOrigen(), item.getDestino(), item.getPrecio(), item.getDistanciaKm());
            }

            System.out.println("\n   -> Listado completo de conductores registrados:");
            List<Conductor> listaConductores = session.createQuery("from Conductor", Conductor.class).list();
            for (Conductor item : listaConductores) {
                System.out.printf("      [Conductor #%d] %s | Cédula: %s | Licencia: %s | Teléfono: %s%n",
                        item.getId(), item.getNombre(), item.getCedula(), item.getLicencia(), item.getTelefono());
            }
        } catch (Exception e) {
            System.err.println(">>> Error durante la fase READ: " + e.getMessage());
            e.printStackTrace();
        }

        // 3. OPERACION UPDATE (Actualizar estado de vehiculo a EN_DESPACHO)
        System.out.println("\n>>> [FASE 3 - UPDATE] Actualizando estado del vehículo...");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Vehiculo v = session.get(Vehiculo.class, vehiculoId);
            if (v != null) {
                v.setEstado("EN_DESPACHO");
                session.merge(v);
                tx.commit();
                System.out.println("   [OK] Estado del vehículo ID " + vehiculoId + " actualizado a: " + v.getEstado());
            }
        } catch (Exception e) {
            System.err.println(">>> Error durante la fase UPDATE: " + e.getMessage());
            e.printStackTrace();
        }

        // 4. CIERRE LIMPIO DE HIBERNATE
        System.out.println("\n>>> [FINALIZADO] Prueba de persistencia JPA y conexión a MySQL completada con éxito.");
        HibernateUtil.shutdown();
        System.out.println("================================================================================");
    }
}
