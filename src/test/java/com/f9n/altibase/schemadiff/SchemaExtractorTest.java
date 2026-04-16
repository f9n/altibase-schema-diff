package com.f9n.altibase.schemadiff;

import com.f9n.altibase.schemadiff.model.SequenceInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchemaExtractorTest {

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private ConnectionConfig config;
    private SchemaExtractor extractor;

    @BeforeEach
    void setUp() {
        // server, port, user, password, database, connectTimeoutSeconds
        config = new ConnectionConfig("server", 1234, "user", "pass", "db", 30);
        extractor = new SchemaExtractor(connection, config, Set.of());
    }

    @Test
    void testExtractSequences() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("GTPDB");
        when(resultSet.getString(2)).thenReturn("SEQ_TEST");

        // Execute
        List<SequenceInfo> sequences = extractor.extractSequences();

        // Verify
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        assertEquals("GTPDB", sequences.get(0).schema());
        assertEquals("SEQ_TEST", sequences.get(0).name());
        assertEquals(0, sequences.get(0).minValue());
        assertFalse(sequences.get(0).hasDetails());
        
        verify(statement).executeQuery(argThat(s -> s != null && s.contains("SYS_TABLES_")));
    }
}
