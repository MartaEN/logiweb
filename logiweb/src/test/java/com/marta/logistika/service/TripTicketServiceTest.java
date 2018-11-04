package com.marta.logistika.service;

import com.marta.logistika.dao.api.DriverDao;
import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.dao.api.TruckDao;
import com.marta.logistika.dto.Instruction;
import com.marta.logistika.entity.*;
import com.marta.logistika.enums.DriverStatus;
import com.marta.logistika.enums.OrderStatus;
import com.marta.logistika.enums.TripTicketStatus;
import com.marta.logistika.event.EntityUpdateEvent;
import com.marta.logistika.exception.checked.NoDriversAvailableException;
import com.marta.logistika.exception.checked.NoRouteFoundException;
import com.marta.logistika.exception.checked.OrderDoesNotFitToTicketException;
import com.marta.logistika.exception.checked.PastDepartureDateException;
import com.marta.logistika.exception.unchecked.UncheckedServiceException;
import com.marta.logistika.service.api.RoadService;
import com.marta.logistika.service.api.TimeTrackerService;
import com.marta.logistika.service.api.TripTicketService;
import com.marta.logistika.service.impl.TripTicketServiceImpl;
import com.marta.logistika.service.impl.helpers.StopoverHelper;
import com.marta.logistika.service.impl.helpers.TripTicketServiceHelper;
import com.marta.logistika.service.impl.helpers.WebsocketUpdateHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class TripTicketServiceTest {

    @Mock
    private TripTicketDao ticketDao;
    @Mock
    private TruckDao truckDao;
    @Mock
    private DriverDao driverDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private RoadService roadService;
    @Mock
    private TimeTrackerService timeTrackerService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private WebsocketUpdateHelper websocketUpdateHelper;

    private TripTicketService ticketService;
    private TripTicketServiceHelper ticketServiceHelper;
    private StopoverHelper stopoverHelper;

    @Captor
    private ArgumentCaptor<TripTicketEntity> tripTicketArgument;

    private CityEntity moscow;
    private CityEntity tver;
    private CityEntity pskov;
    private DriverEntity kozlevich;
    private DriverEntity balaganov;
    private DriverEntity panikovski;
    private TruckEntity truck1free;
    private TruckEntity truck2bookedForTicket;
    private OrderEntity order1;
    private OrderEntity order2;
    private OrderEntity order3;
    private TripTicketEntity ticket1;
    private TripTicketEntity ticket2;
    final static LocalDateTime MAX_FUTURE_DATE = LocalDateTime.of(2099, 12, 31, 23, 59);

    @Before
    public void setup() throws Exception {
        this.stopoverHelper = new StopoverHelper(roadService);
        this.ticketServiceHelper = new TripTicketServiceHelper(
                ticketDao,
                roadService,
                timeTrackerService,
                stopoverHelper,
                applicationEventPublisher);
        this.ticketService = new TripTicketServiceImpl(
                ticketDao,
                truckDao,
                driverDao,
                orderDao,
                ticketServiceHelper,
                websocketUpdateHelper,
                applicationEventPublisher);
        prefillTestData();
        defineMocks();
    }

    @Test
    public void createReturnTicket() {
        LocalDateTime departureDate = LocalDateTime.now().plusDays(1);
        ticketService.createTicket("AA00000", departureDate, null);

        Mockito.verify(ticketDao).add(tripTicketArgument.capture());
        Assert.assertEquals(TripTicketStatus.CREATED, tripTicketArgument.getValue().getStatus());
        Assert.assertEquals(0, tripTicketArgument.getValue().getCurrentStep());
        Assert.assertEquals(truck1free, tripTicketArgument.getValue().getTruck());
        Assert.assertEquals(MAX_FUTURE_DATE, tripTicketArgument.getValue().getTruck().getBookedUntil());
        Assert.assertFalse(tripTicketArgument.getValue().getTruck().isParked());
        Assert.assertEquals(moscow, tripTicketArgument.getValue().getTruck().getLocation());
        Assert.assertEquals(departureDate, tripTicketArgument.getValue().getDepartureDateTime());
        Assert.assertEquals(2, tripTicketArgument.getValue().getStopovers().size());
        Assert.assertEquals(0, tripTicketArgument.getValue().getStopoversSorted().get(0).getSequenceNo());
        Assert.assertEquals(moscow, tripTicketArgument.getValue().getStopoversSorted().get(0).getCity());
        Assert.assertEquals(1, tripTicketArgument.getValue().getStopoversSorted().get(1).getSequenceNo());
        Assert.assertEquals(moscow, tripTicketArgument.getValue().getStopoversSorted().get(1).getCity());
        Mockito.verify(applicationEventPublisher, VerificationModeFactory.times(1)).publishEvent(Mockito.any(EntityUpdateEvent.class));
    }

    @Test
    public void createOneWayTicket() {
        LocalDateTime departureDate = LocalDateTime.now().plusDays(1);
        ticketService.createTicket("AA00000", departureDate, tver);

        Mockito.verify(ticketDao).add(tripTicketArgument.capture());
        Assert.assertEquals(TripTicketStatus.CREATED, tripTicketArgument.getValue().getStatus());
        Assert.assertEquals(0, tripTicketArgument.getValue().getCurrentStep());
        Assert.assertEquals(truck1free, tripTicketArgument.getValue().getTruck());
        Assert.assertEquals(MAX_FUTURE_DATE, tripTicketArgument.getValue().getTruck().getBookedUntil());
        Assert.assertFalse(tripTicketArgument.getValue().getTruck().isParked());
        Assert.assertEquals(tver, tripTicketArgument.getValue().getTruck().getLocation());
        Assert.assertEquals(departureDate, tripTicketArgument.getValue().getDepartureDateTime());
        Assert.assertEquals(2, tripTicketArgument.getValue().getStopovers().size());
        Assert.assertEquals(0, tripTicketArgument.getValue().getStopoversSorted().get(0).getSequenceNo());
        Assert.assertEquals(moscow, tripTicketArgument.getValue().getStopoversSorted().get(0).getCity());
        Assert.assertEquals(1, tripTicketArgument.getValue().getStopoversSorted().get(1).getSequenceNo());
        Assert.assertEquals(tver, tripTicketArgument.getValue().getStopoversSorted().get(1).getCity());
        Mockito.verify(applicationEventPublisher, VerificationModeFactory.times(1)).publishEvent(Mockito.any(EntityUpdateEvent.class));
    }

    @Test(expected = UncheckedServiceException.class)
    public void createPastDateTicket() {
        LocalDateTime departureDate = LocalDateTime.now().minusDays(1);
        ticketService.createTicket("AA00000", departureDate, null);
        Mockito.verify(ticketDao, VerificationModeFactory.times(0)).add(Mockito.any(TripTicketEntity.class));
        Mockito.verify(applicationEventPublisher, VerificationModeFactory.times(0)).publishEvent(Mockito.any(EntityUpdateEvent.class));
    }

    @Test
    public void addOrderToTicket() throws Exception {
        ticketServiceHelper.addOrderToTicket(order1, ticket1);
        Assert.assertEquals(3, ticket1.getStopovers().size());
        Assert.assertEquals(0, ticket1.getStopoversSorted().get(0).getSequenceNo());
        Assert.assertEquals(tver, ticket1.getStopoversSorted().get(0).getCity());
        Assert.assertEquals(1, ticket1.getStopoversSorted().get(1).getSequenceNo());
        Assert.assertEquals(pskov, ticket1.getStopoversSorted().get(1).getCity());
        Assert.assertEquals(2, ticket1.getStopoversSorted().get(2).getSequenceNo());
        Assert.assertEquals(tver, ticket1.getStopoversSorted().get(2).getCity());
        Assert.assertEquals(1, ticket1.getStopoverWithSequenceNo(0).getLoads().size());
        Assert.assertEquals(0, ticket1.getStopoverWithSequenceNo(0).getUnloads().size());
        Assert.assertEquals(0, ticket1.getStopoverWithSequenceNo(1).getLoads().size());
        Assert.assertEquals(1, ticket1.getStopoverWithSequenceNo(1).getUnloads().size());
        Assert.assertEquals(0, ticket1.getStopoverWithSequenceNo(2).getLoads().size());
        Assert.assertEquals(0, ticket1.getStopoverWithSequenceNo(2).getUnloads().size());
        Assert.assertEquals(OrderStatus.ASSIGNED, order1.getStatus());
        Assert.assertEquals(17, ticket1.getAvgLoad());
        Mockito.verify(applicationEventPublisher, VerificationModeFactory.times(1)).publishEvent(Mockito.any(EntityUpdateEvent.class));
    }

    @Test (expected = OrderDoesNotFitToTicketException.class)
    public void addTooBigOrderToTicket() throws NoRouteFoundException, OrderDoesNotFitToTicketException {
        ticketServiceHelper.addOrderToTicket(order3, ticket2);
    }

    @Test
    public void removeOrderFromTicket() {
        order1.setStatus(OrderStatus.ASSIGNED);
        order2.setStatus(OrderStatus.ASSIGNED);

        // The ticket route is tver - pskov - moscow, and
        // the ticket holds two orders: tver - pskov and tver - moscow.
        // We first remove the tver-moscow order
        ticketService.removeOrderFromTicket(2L, 20L);
        Assert.assertEquals(3, ticket2.getStopovers().size());
        Assert.assertEquals(0, ticket2.getStopoversSorted().get(0).getSequenceNo());
        Assert.assertEquals(tver, ticket2.getStopoversSorted().get(0).getCity());
        Assert.assertEquals(1, ticket2.getStopoversSorted().get(1).getSequenceNo());
        Assert.assertEquals(pskov, ticket2.getStopoversSorted().get(1).getCity());
        Assert.assertEquals(2, ticket2.getStopoversSorted().get(2).getSequenceNo());
        Assert.assertEquals(moscow, ticket2.getStopoversSorted().get(2).getCity());
        Assert.assertEquals(1, ticket2.getStopoverWithSequenceNo(0).getLoads().size());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(0).getUnloads().size());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(1).getLoads().size());
        Assert.assertEquals(1, ticket2.getStopoverWithSequenceNo(1).getUnloads().size());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(2).getLoads().size());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(2).getUnloads().size());
        Assert.assertEquals(OrderStatus.NEW, order2.getStatus());
        Assert.assertEquals(15, ticket2.getAvgLoad());

        // Now we remove the tver - pskov order
        ticketService.removeOrderFromTicket(2L, 10L);
        Assert.assertEquals(2, ticket2.getStopovers().size());
        Assert.assertEquals(0, ticket2.getStopoversSorted().get(0).getSequenceNo());
        Assert.assertEquals(tver, ticket2.getStopoversSorted().get(0).getCity());
        Assert.assertEquals(1, ticket2.getStopoversSorted().get(1).getSequenceNo());
        Assert.assertEquals(moscow, ticket2.getStopoversSorted().get(1).getCity());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(0).getLoads().size());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(0).getUnloads().size());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(1).getLoads().size());
        Assert.assertEquals(0, ticket2.getStopoverWithSequenceNo(1).getUnloads().size());
        Assert.assertEquals(OrderStatus.NEW, order1.getStatus());
        Assert.assertEquals(0, ticket2.getAvgLoad());
    }

    @Test
    public void approveTicket() throws PastDepartureDateException, NoDriversAvailableException {
        ticketService.approveTicket(2L);
        Assert.assertEquals(TripTicketStatus.APPROVED, ticket2.getStatus());
        Assert.assertEquals(2, ticket2.getDrivers().size());
        Assert.assertTrue(ticket2.getDrivers().contains(kozlevich));
        Assert.assertTrue(ticket2.getDrivers().contains(balaganov));
        Assert.assertEquals(ticket2.getDepartureDateTime().plusHours(29), ticket2.getArrivalDateTime());
        Assert.assertEquals(ticket2.getDepartureDateTime().plusHours(29), kozlevich.getBookedUntil());
        Assert.assertEquals(ticket2.getDepartureDateTime().plusHours(29), balaganov.getBookedUntil());
        Assert.assertEquals(ticket2.getDepartureDateTime().plusHours(29), truck2bookedForTicket.getBookedUntil());
        Assert.assertEquals(OrderStatus.READY_TO_SHIP, order1.getStatus());
        Assert.assertEquals(OrderStatus.READY_TO_SHIP, order2.getStatus());
        Mockito.verify(websocketUpdateHelper, VerificationModeFactory.times(2)).sendUpdate(Mockito.any(String.class), Mockito.any(Object.class));
        Mockito.verify(applicationEventPublisher, VerificationModeFactory.times(1)).publishEvent(Mockito.any(EntityUpdateEvent.class));
    }

    @Test
    public void testDeliveryCycle () throws PastDepartureDateException, NoDriversAvailableException {
        ticketService.approveTicket(2L);

        Principal kozlevichPrincipal = () -> "akozlevich";
        Principal balaganovPrincipal = () -> "abalaganov";

        // get first instruction for kozlevich
        Instruction instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.START, instruction.getTask());
        Assert.assertEquals(ticket2.getId(), instruction.getTicket().getId());

        // kozlevich opens his shift
        ticketService.setOnline(kozlevichPrincipal, 2L);
        Assert.assertEquals(DriverStatus.HANDLING, kozlevich.getStatus());
        Assert.assertEquals(TripTicketStatus.RUNNING, ticket2.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.LOAD, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.START, instruction.getTask());
//        Mockito.verify(websocketUpdateHelper, VerificationModeFactory.times(1)).sendUpdate(Mockito.any(String.class), Mockito.any(Object.class));

        // kozlevich loads the cargo at the departure city and turns stuck waiting for balaganov
        ticketService.loadAtStopover(kozlevichPrincipal, 2L, 0);
        Assert.assertEquals(OrderStatus.SHIPPED, order1.getStatus());
        Assert.assertEquals(OrderStatus.SHIPPED, order2.getStatus());
        Assert.assertEquals(DriverStatus.WAITING, kozlevich.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.WAIT, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.START, instruction.getTask());

        // balaganov opens his shift
        ticketService.setOnline(balaganovPrincipal, 2L);
        Assert.assertEquals(DriverStatus.DRIVING, balaganov.getStatus());
        Assert.assertEquals(DriverStatus.SECONDING, kozlevich.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());

        // kozlevich takes the drivers place
        ticketService.setFirstDriver(kozlevichPrincipal, 2L);
        Assert.assertEquals(DriverStatus.DRIVING, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.SECONDING, balaganov.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());

        // balaganov makes a road break
        ticketService.startRoadBreak(balaganovPrincipal, 2L);
        Assert.assertEquals(DriverStatus.ROAD_BREAK, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.ROAD_BREAK, balaganov.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.FINISH_ROAD_BREAK, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.FINISH_ROAD_BREAK, instruction.getTask());

        // kozlevich finishes the road break
        ticketService.finishRoadBreak(kozlevichPrincipal, 2L);
        Assert.assertEquals(DriverStatus.DRIVING, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.SECONDING, balaganov.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());

        // kozlevich reports arrival to pskov
        ticketService.reachStopover(kozlevichPrincipal, 2L, 1);
        Assert.assertEquals(DriverStatus.HANDLING, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.HANDLING, balaganov.getStatus());
        Assert.assertEquals(1, ticket2.getCurrentStep());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.UNLOAD, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.UNLOAD, instruction.getTask());

        // balaganov takes a stopover break
        ticketService.startStopoverBreak(balaganovPrincipal);
        Assert.assertEquals(DriverStatus.HANDLING, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.STOPOVER_BREAK, balaganov.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.UNLOAD, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.FINISH_STOPOVER_BREAK, instruction.getTask());

        // kozlevich reports unload and turns stuck waiting for balaganov
        ticketService.unloadAtStopover(kozlevichPrincipal, 2L, 1);
        Assert.assertEquals(OrderStatus.DELIVERED, order1.getStatus());
        Assert.assertEquals(OrderStatus.SHIPPED, order2.getStatus());
        Assert.assertEquals(DriverStatus.WAITING, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.STOPOVER_BREAK, balaganov.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.WAIT, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.FINISH_STOPOVER_BREAK, instruction.getTask());

        // balaganov finishes the stopover break
        ticketService.finishStopoverBreak(balaganovPrincipal, 2L);
        Assert.assertEquals(DriverStatus.DRIVING, balaganov.getStatus());
        Assert.assertEquals(DriverStatus.SECONDING, kozlevich.getStatus());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.GOTO, instruction.getTask());

        // kozlevich reports arrival to moscow
        ticketService.reachStopover(kozlevichPrincipal, 2L, 2);
        Assert.assertEquals(DriverStatus.HANDLING, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.HANDLING, balaganov.getStatus());
        Assert.assertEquals(2, ticket2.getCurrentStep());
        instruction = ticketService.getInstructionForDriver(kozlevichPrincipal);
        Assert.assertEquals(Instruction.Task.UNLOAD, instruction.getTask());
        instruction = ticketService.getInstructionForDriver(balaganovPrincipal);
        Assert.assertEquals(Instruction.Task.UNLOAD, instruction.getTask());

        // kozlevich reports unload at moscow
        ticketService.unloadAtStopover(kozlevichPrincipal, 2L, 2);
        Assert.assertEquals(OrderStatus.DELIVERED, order2.getStatus());
        Assert.assertEquals(DriverStatus.OFFLINE, kozlevich.getStatus());
        Assert.assertEquals(DriverStatus.OFFLINE, balaganov.getStatus());
    }

    @Test
    public void listAllOrdersInTicket() {
        List<OrderEntity> orders = ticketService.listAllOrdersInTicket(2L);
        Assert.assertEquals(2, orders.size());
        Assert.assertTrue(orders.contains(order1));
        Assert.assertTrue(orders.contains(order2));
    }

    private void prefillTestData() {
        moscow = new CityEntity();
        moscow.setId(1);
        moscow.setName("Москва");

        tver = new CityEntity();
        tver.setId(2);
        tver.setName("Тверь");

        pskov = new CityEntity();
        pskov.setId(6);
        pskov.setName("Псков");

        kozlevich = new DriverEntity();
        kozlevich.setId(1L);
        kozlevich.setPersonalId("000001");
        kozlevich.setUsername("akozlevich");
        kozlevich.setLocation(tver);
        kozlevich.setStatus(DriverStatus.OFFLINE);

        balaganov = new DriverEntity();
        balaganov.setId(2L);
        balaganov.setPersonalId("000002");
        balaganov.setUsername("abalaganov");
        balaganov.setLocation(tver);
        balaganov.setStatus(DriverStatus.OFFLINE);

        panikovski = new DriverEntity();
        panikovski.setId(3L);
        panikovski.setPersonalId("000003");
        panikovski.setUsername("mpanikovski");
        panikovski.setLocation(pskov);
        panikovski.setStatus(DriverStatus.OFFLINE);

        truck1free = new TruckEntity();
        truck1free.setId(1L);
        truck1free.setRegNumber("AA00000");
        truck1free.setLocation(moscow);
        truck1free.setCapacity(5000);
        truck1free.setShiftSize(1);
        truck1free.setParked(true);
        truck1free.setServiceable(true);
        truck1free.setBookedUntil(LocalDate.now().minusDays(1).atStartOfDay());

        truck2bookedForTicket = new TruckEntity();
        truck2bookedForTicket.setId(2L);
        truck2bookedForTicket.setRegNumber("BB00000");
        truck2bookedForTicket.setLocation(tver);
        truck2bookedForTicket.setCapacity(20000);
        truck2bookedForTicket.setShiftSize(2);
        truck2bookedForTicket.setParked(false);
        truck2bookedForTicket.setServiceable(true);
        truck2bookedForTicket.setBookedUntil(MAX_FUTURE_DATE);

        //change status in test method before running tests when necessary
        order1 = new OrderEntity();
        order1.setId(10L);
        order1.setStatus(OrderStatus.NEW);
        order1.setWeight(7000);
        order1.setFromCity(tver);
        order1.setToCity(pskov);

        //change status in test method before running tests when necessary
        order2 = new OrderEntity();
        order2.setId(20L);
        order2.setStatus(OrderStatus.NEW);
        order2.setWeight(5000);
        order2.setFromCity(tver);
        order2.setToCity(moscow);

        //change status in test method before running tests when necessary
        order3 = new OrderEntity();
        order3.setId(30L);
        order3.setStatus(OrderStatus.NEW);
        order3.setWeight(20000);
        order3.setFromCity(tver);
        order3.setToCity(moscow);

        ticket1 = new TripTicketEntity();
        ticket1.setId(1L);
        ticket1.setStatus(TripTicketStatus.CREATED);
        ticket1.setTruck(truck2bookedForTicket);
        StopoverEntity stopover1_0 = new StopoverEntity();
        stopover1_0.setId(1L);
        stopover1_0.setSequenceNo(0);
        stopover1_0.setCity(tver);
        StopoverEntity stopover1_1 = new StopoverEntity();
        stopover1_1.setId(2L);
        stopover1_1.setSequenceNo(1);
        stopover1_1.setCity(tver);
        ticket1.setStopovers(new HashSet<>(Arrays.asList(stopover1_0, stopover1_1)));

        ticket2 = new TripTicketEntity();
        ticket2.setDepartureDateTime(LocalDate.now().plusDays(2).atStartOfDay());
        ticket2.setId(2L);
        ticket2.setStatus(TripTicketStatus.CREATED);
        ticket2.setTruck(truck2bookedForTicket);
        StopoverEntity stopover2_0 = new StopoverEntity();
        stopover2_0.setId(1L);
        stopover2_0.setSequenceNo(0);
        stopover2_0.setCity(tver);
        StopoverEntity stopover2_1 = new StopoverEntity();
        stopover2_1.setId(2L);
        stopover2_1.setSequenceNo(1);
        stopover2_1.setCity(pskov);
        StopoverEntity stopover2_2 = new StopoverEntity();
        stopover2_2.setId(3L);
        stopover2_2.setSequenceNo(2);
        stopover2_2.setCity(moscow);
        ticket2.setStopovers(new HashSet<>(Arrays.asList(stopover2_0, stopover2_1, stopover2_2)));
        TransactionLoadEntity load1 = new TransactionLoadEntity();
        load1.setId(1L);
        load1.setOrder(order1);
        stopover2_0.getLoads().add(load1);
        TransactionUnloadEntity unload1 = new TransactionUnloadEntity();
        unload1.setId(2L);
        unload1.setOrder(order1);
        stopover2_1.getUnloads().add(unload1);
        TransactionLoadEntity load2 = new TransactionLoadEntity();
        load2.setId(3L);
        load2.setOrder(order2);
        stopover2_0.getLoads().add(load2);
        TransactionUnloadEntity unload2 = new TransactionUnloadEntity();
        unload2.setId(4L);
        unload2.setOrder(order2);
        stopover2_2.getUnloads().add(unload2);
    }

    private void defineMocks() throws NoRouteFoundException {
        Mockito.when(truckDao.findByRegNumber("AA00000")).thenReturn(truck1free);
        Mockito.when(roadService.getDistanceFromTo(tver, pskov)).thenReturn(600);
        Mockito.when(roadService.getDistanceFromTo(pskov, tver)).thenReturn(600);
        Mockito.when(roadService.getDistanceFromTo(moscow, pskov)).thenReturn(800);
        Mockito.when(roadService.getDistanceFromTo(pskov, moscow)).thenReturn(800);
        Mockito.when(roadService.getRouteDistance(Arrays.asList(tver, pskov, tver))).thenReturn(1200);
        Mockito.when(ticketDao.findById(1L)).thenReturn(ticket1);
        Mockito.when(ticketDao.findById(2L)).thenReturn(ticket2);
        Mockito.when(orderDao.findById(10L)).thenReturn(order1);
        Mockito.when(orderDao.findById(20L)).thenReturn(order2);
        Mockito.when(orderDao.findById(30L)).thenReturn(order3);
        Mockito.when(driverDao.listAllAvailable(tver, ticket2.getDepartureDateTime())).thenReturn(Arrays.asList(kozlevich, balaganov));
        Mockito.when(driverDao.findByUsername("akozlevich")).thenReturn(kozlevich);
        Mockito.when(driverDao.findByUsername("abalaganov")).thenReturn(balaganov);
        Mockito.when(driverDao.findByUsername("mpanikovski")).thenReturn(panikovski);
        Mockito.when(driverDao.findByPersonalId("000001")).thenReturn(kozlevich);
        Mockito.when(driverDao.findByPersonalId("000002")).thenReturn(balaganov);
        Mockito.when(driverDao.findByPersonalId("000003")).thenReturn(panikovski);
        Mockito.when(ticketDao.findByDriverAndStatus("000001", TripTicketStatus.APPROVED)).thenReturn(ticket2);
        Mockito.when(ticketDao.findByDriverAndStatus("000001", TripTicketStatus.RUNNING)).thenReturn(ticket2);
        Mockito.when(ticketDao.findByDriverAndStatus("000002", TripTicketStatus.APPROVED)).thenReturn(ticket2);
        Mockito.when(ticketDao.findByDriverAndStatus("000002", TripTicketStatus.RUNNING)).thenReturn(ticket2);
    }

}
