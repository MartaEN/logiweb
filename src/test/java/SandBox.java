import com.marta.logistika.configuration.ApplicationConfig;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.OrderService;
import com.marta.logistika.service.api.TripTicketService;
import com.marta.logistika.service.api.TruckService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;


// comment the @Configuration line in WebConfig to run this sandbox app in console
public class SandBox {

    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        TripTicketService tripTicketService = ctx.getBean("tripTicketService", TripTicketService.class);
        TruckService truckService = ctx.getBean("truckService", TruckService.class);
        OrderService orderService = ctx.getBean("orderService", OrderService.class);
        CityService cityService = ctx.getBean("cityService", CityService.class);

//        OrderEntity order = new OrderEntity();
//        order.setFromCity(cityService.findById(1L));
//        order.setToCity(cityService.findById(7L));
//        order.setDescription("First order in the system");
//        order.setWeight(3500);
//        orderService.add(order);


//        TripTicketEntity ticket = tripTicketService.createTripTicket(truckService.findTruckByRegNum("AA00000"), LocalDateTime.now().plusDays(1));
//
//        OrderEntity order1 = orderService.findById(1L);
//        OrderEntity order2 = orderService.findById(2L);
//
//        tripTicketService.addOrderToTicket(ticket, order1);
//        tripTicketService.addOrderToTicket(ticket, order2);

        TripTicketEntity ticket = tripTicketService.findById(1L);
        tripTicketService.approveTripTicket(ticket);

    }


}
