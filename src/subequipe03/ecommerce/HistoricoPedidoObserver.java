package subequipe03.ecommerce;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observador de auditoria: guarda cada transição de estado com data e hora,
 * formando o histórico do pedido.
 */
public class HistoricoPedidoObserver implements PedidoObserver {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final List<String> registros = new ArrayList<>();

    @Override
    public void atualizar(Pedido pedido, String estadoAnterior) {
        registros.add(LocalDateTime.now().format(FORMATO) + " | Pedido #" + pedido.getIdPedido()
                + " | " + estadoAnterior + " -> " + pedido.getStatusPedido());
    }

    public List<String> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    public void imprimirHistorico() {
        System.out.println("   --- Histórico do pedido ---");
        registros.forEach(r -> System.out.println("   " + r));
    }
}
