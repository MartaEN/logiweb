package com.marta.logistika.service.api;

import com.marta.logistika.dto.Instruction;
import com.marta.logistika.dto.SystemMessage;
import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.exception.checked.NoDriversAvailableException;
import com.marta.logistika.exception.checked.PastDepartureDateException;
import org.springframework.lang.Nullable;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface TripTicketService {

    // **********************************************************************//
    // *   creating, filling and approving the ticket (office operations)   *//
    // **********************************************************************//


    /**
     * Method creates new trip ticket and books the truck.
     * Departure city is defined as truck location city.
     * Arrival city, if not specified, is set the same as departure city (round-trip).
     *
     * @param truckRegNum       truck to be booked
     * @param departureDateTime departure date and time
     * @param toCity            destination city (may be omitted)
     */
    long createTicket(String truckRegNum, LocalDateTime departureDateTime, @Nullable CityEntity toCity);

    /**
     * Updates departure date and time in the trip ticket
     *
     * @param ticketId          ticket id
     * @param departureDateTime new departure date and time
     */
    void updateDepartureDateTime(long ticketId, LocalDateTime departureDateTime);

    /**
     * Attempts adding the order to the trip ticket and returns a message with operation result
     *
     * @param ticketId target trip ticket id
     * @param orderId  order id
     * @param locale   user locale
     * @return system message with the result of operation
     */
    SystemMessage addSingleOrderToTicketAndReport(long ticketId, long orderId, Locale locale);

    /**
     * Attempts adding several orders to a trip ticket, and tells how many additions were successful.
     * Orders to be added are selected by given criteria.
     *
     * @param fromCityId order selection criteria - departure city id
     * @param toCityId   order selection criteria - destination city id
     * @param date       order selection criteria - order creation date
     * @param ticketId   target trip ticket id
     * @param locale     user's locale
     * @return system message with the result of operation
     */
    SystemMessage addMultipleOrdersToTicketAndReport(long fromCityId, long toCityId, @Nullable LocalDate date, long ticketId, Locale locale);

    /**
     * Method removes order from the ticket and updates route and truck load data.
     *
     * @param ticketId ticket id
     * @param orderId  order id
     */
    void removeOrderFromTicket(long ticketId, long orderId);

    /**
     * Method finalizes trip ticket and marks it as approved for execution.
     * Finalization includes correction of the departure date to the future (if necessary)
     * and assigning the drivers.
     *
     * @param ticketId ticket id to be approved
     * @throws PastDepartureDateException  in case ticket departure date is in the past
     * @throws NoDriversAvailableException in case no drivers are available for the ticket
     */
    void approveTicket(long ticketId) throws PastDepartureDateException, NoDriversAvailableException;

    // ******************************************************//
    // *  processing ticket on the road (driver operations) *//
    // ******************************************************//

    /**
     * Checks if there are any trip tickets with APPROVED or RUNNING status assigned to the driver
     *
     * @param driver driver
     * @return true if there are such trip tickets
     */
    boolean hasAnyOpenTickets(DriverEntity driver);

    /**
     * Method looks up for a current trip ticket assigned to the driver
     * and compiles the instruction to the driver based on current ticket step and its loads / unloads list
     *
     * @param principal driver initiating the request
     * @return instruction to the driver usable in OnTheRoadController
     */
    Instruction getInstructionForDriver(Principal principal);

    /**
     * Method records driver going online (opening a new shift)
     *
     * @param principal requesting driver
     * @param ticketId  ticket id
     */
    void setOnline(Principal principal, long ticketId);

    /**
     * Method records finished move to the new stopover and updates time tracking for all the drivers
     *
     * @param ticketId ticket id
     * @param step     sequence number of the stopover to move to
     */
    void reachStopover(Principal principal, long ticketId, int step);

    /**
     * Method marks all load operations at the given stopover as completed
     * (order statuses changed to shipped), and updates time tracking for the driver
     *
     * @param ticketId ticket id
     * @param step     sequence number of the stopover
     */
    void loadAtStopover(Principal principal, long ticketId, int step);

    /**
     * Method marks all unload operations at the given stopover as completed
     * (order statuses changed to delivered), and updates time tracking for the driver
     *
     * @param ticketId ticket id
     * @param step     sequence number of the stopover
     */
    void unloadAtStopover(Principal principal, long ticketId, int step);

    /**
     * Method registers the reporting driver as the first (driving) one, and changes all the rest to seconding status.
     *
     * @param principal reporting driver
     * @param ticketId  ticket id
     */
    void setFirstDriver(Principal principal, long ticketId);

    /**
     * Method registers drivers taking a road break (status ROAD_BREAK is assigned to all truck drivers).
     *
     * @param principal reporting driver
     * @param ticketId  ticket id
     */
    void startRoadBreak(Principal principal, long ticketId);

    /**
     * Method registers drivers finishing a road break (status RESTING is assigned to all truck drivers).
     *
     * @param principal reporting driver
     * @param ticketId  ticket id
     */
    void finishRoadBreak(Principal principal, long ticketId);

    /**
     * Method registers drivers taking a stopover break (break status is assigned to him but not to other truck drivers).
     *
     * @param principal reporting driver
     */
    void startStopoverBreak(Principal principal);

    /**
     * Method registers driver's stopover break is over
     *
     * @param principal reporting driver
     */
    void finishStopoverBreak(Principal principal, long ticketId);

    // ******************************************************//
    // *            other very useful operations            *//
    // ******************************************************//

    /**
     * finds trip ticket by id
     *
     * @param ticketId ticket id
     * @return ticket
     */
    TripTicketRecord findById(long ticketId);

    /**
     * lists all trip tickets
     *
     * @return ticket list
     */
    List<TripTicketRecord> listAll();

    /**
     * lists all created but unapproved trip tickets
     *
     * @return ticket list
     */
    List<TripTicketRecord> listAllUnapproved();

    /**
     * lists all orders assigned to the ticket
     *
     * @param id ticket id
     * @return orders list
     */
    List<OrderEntity> listAllOrdersInTicket(long id);

    /**
     * calculates planned trip ticket execution time, in minutes, split by year and month
     *
     * @param ticket trip ticket
     * @return map with planned trip ticket execution time by monthly periods
     */
    Map<YearMonth, Long> getPlannedMinutesByYearMonth(TripTicketEntity ticket);

    /**
     * calculates minimum date / time that the ticket's departure datetime can be changed to
     *
     * @param ticketId trip ticket
     * @return minimum date / time that the ticket's departure datetime can be changed to
     */
    LocalDateTime getMinNewDateTime(long ticketId);

}
