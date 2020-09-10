import core.Line;
import core.Station;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import junit.framework.TestCase;

public class RouteCalculatorTest extends TestCase {

    RouteCalculator calculator;
    StationIndex index;
    List<Station> route;
    Line line1;
    Line line2;
    Line line3;

    Station station1;
    Station station2;
    Station station3;
    Station station4;
    Station station5;
    Station station6;
    Station station7;
    Station station8;

    TreeMap<Station, TreeSet<Station>> connections;


    protected void setUp() throws Exception {
        TreeSet<Station> connectedStations1 = new TreeSet<>();
        TreeSet<Station> connectedStations2 = new TreeSet<>();
        connections = new TreeMap<>();
        route = new ArrayList<>();
        index = new StationIndex();
        line1 = new Line(1, "First");
        line2 = new Line(2, "Second");
        line3 = new Line(3, "Third");

        station1 = new Station("first", line1);
        station2 = new Station("second", line1);
        station3 = new Station("third", line2);
        station4 = new Station("forth", line2);
        station5 = new Station("fifth", line3);
        station6 = new Station("sixth", line3);
        station7 = new Station("seventh", line1);
        station8 = new Station("eighth", line1);

        line1.addStation(station1);
        line1.addStation(station2);
        line1.addStation(station7);
        line1.addStation(station8);
        line2.addStation(station3);
        line2.addStation(station4);
        line3.addStation(station5);
        line3.addStation(station6);

        connectedStations1.add(station1);
        connectedStations2.add(station2);
        connectedStations2.add(station3);
        connectedStations1.add(station5);
        connections.put(station1, connectedStations1);
        connections.put(station5, connectedStations1);
        connections.put(station2, connectedStations2);
        connections.put(station3, connectedStations2);

        route.add(station1);
        route.add(station2);
        route.add(station3);
        route.add(station4);
        route.add(station5);
        route.add(station6);

        index.addLine(line1);
        index.addLine(line2);
        index.addLine(line3);
        index.connections = connections;
        index.addStation(station1);
        index.addStation(station2);
        index.addStation(station3);
        index.addStation(station4);
        index.addStation(station5);
        index.addStation(station6);
        index.addStation(station7);
        index.addStation(station8);
        calculator = new RouteCalculator(index);
    }


    public void testCalculateDuration() {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 14.5;
        assertEquals(expected, actual);
    }

    public void testGetRouteOnTheLine() {
        List<Station> actual = calculator.getRouteOnTheLine(station1, station8);
        List<Station> expected = new ArrayList<>();
        expected.add(station1);
        expected.add(station2);
        expected.add(station7);
        expected.add(station8);
        assertEquals(expected, actual);
    }

    public void testGetRouteWithOneConnection() {
        List<Station> actual = calculator.getRouteWithOneConnection(station1, station4);
        List<Station> expected = new ArrayList<>();
        expected.add(station1);
        expected.add(station2);
        expected.add(station3);
        expected.add(station4);
        assertEquals(expected, actual);
    }

    public void testGetRouteViaConnectedLine() {
        List<Station> actual = calculator.getRouteViaConnectedLine(station2, station5);
        List<Station> expected = new ArrayList<>();
        expected.add(station2);
        expected.add(station1);
        assertEquals(expected, actual);
    }

    public void testGetRouteWithTwoConnections() {
        List<Station> actual = calculator.getRouteWithTwoConnections(station4, station6);
        List<Station> expected = new ArrayList<>();
        expected.add(station4);
        expected.add(station3);
        expected.add(station2);
        expected.add(station1);
        expected.add(station5);
        expected.add(station6);
        assertEquals(expected, actual);
    }

    public void testIsConnected(){
        boolean actual = calculator.isConnected(station2, station3);
        assertTrue(actual);
    }

    public void testGetShortestRoute(){
        List<Station> actual = calculator.getShortestRoute(station4, station6);
        List<Station> expected = new ArrayList<>();
        expected.add(station4);
        expected.add(station3);
        expected.add(station2);
        expected.add(station1);
        expected.add(station5);
        expected.add(station6);
        assertEquals(expected, actual);
    }
}