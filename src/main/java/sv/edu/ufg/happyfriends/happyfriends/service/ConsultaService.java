package sv.edu.ufg.happyfriends.happyfriends.service;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ufg.happyfriends.happyfriends.entity.Consulta;
import sv.edu.ufg.happyfriends.happyfriends.entity.Expediente;
import sv.edu.ufg.happyfriends.happyfriends.entityConverters.PostResponseConverter;
import sv.edu.ufg.happyfriends.happyfriends.exceptionClass.CustomException;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public PostResponseConverter insertConsulta(Consulta consulta) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_add_expediente");

            // Registrar parámetros
            query.registerStoredProcedureParameter("p_EMP_ID", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_EXP_ID", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_CON_SINTOMAS", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_CON_DIAGNOSTICO", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_CON_EXAMENES", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_CON_OBSERVACIONES", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_USU_CODIGO", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_INSERT_RESPONSE", String.class, ParameterMode.OUT);

            // Establecer valores
            query.setParameter("p_EMP_ID", consulta.getEmpId());
            query.setParameter("p_EXP_ID", consulta.getExpId());
            query.setParameter("p_CON_SINTOMAS", consulta.getConSintomas());
            query.setParameter("p_CON_DIAGNOSTICO", consulta.getConDiagnostico());
            query.setParameter("p_CON_EXAMENES", consulta.getConExamenes());
            query.setParameter("p_CON_OBSERVACIONES", consulta.getConObservaciones());
            query.setParameter("p_USU_CODIGO", consulta.getUsuCodigo());

            // Ejecutar el procedimiento
            query.execute();

            String repuesta = (String) query.getOutputParameterValue("p_INSERT_RESPONSE");
            return new PostResponseConverter("", repuesta);


        } catch (PersistenceException ex) {
            // Manejar errores de la base de datos, como problemas de conexión o constraints
            throw new CustomException("Error al insertar la consulta en la base de datos, causa: ", ex);
        } catch (Exception ex) {
            // Manejar cualquier otra excepción
            throw new CustomException("Error inesperado al insertar consulta, causa: ", ex);
        }
    }
}