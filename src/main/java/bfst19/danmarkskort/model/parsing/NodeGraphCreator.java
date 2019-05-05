package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.DrawableModel;
import bfst19.danmarkskort.model.PolyRoad;
import bfst19.danmarkskort.model.RoadRestriction;

import java.util.*;

public class NodeGraphCreator {
    DrawableModel drawableModel;
    private List<OSMRoadNode> roadNodes = new ArrayList<>();
    private Set<OSMRoadWay> osmRoads = new HashSet<>();
    private Map<OSMRoadWay, PolyRoad> roadWaysToPolyRoads = new HashMap<>();
    private Map<PolyRoad, Integer> polyRoadToIntegers = new HashMap<>();


    public NodeGraphCreator(DrawableModel drawableModel) {
        this.drawableModel = drawableModel;
    }


    public void initPolyRoadConnections() {
        for (OSMRoadNode node : roadNodes) {
            osmRoads.addAll(node.getConnections());
        }
        osmRoads.addAll(splitWays());
        createPolyRoadsFromOSMRoads();
        fillPolyRoadsIntoArray();
        initializeConnections();
        removeDuplicates();
    }

    private void removeDuplicates() {
        for (PolyRoad polyRoad : PolyRoad.getAllPolyRoads()) {
            if (polyRoad != null) {
                polyRoad.removeDuplicateConnections();
            }
        }
    }

    private void initializeConnections() {
        for (OSMRoadWay way : roadWaysToPolyRoads.keySet()) {
            PolyRoad road = roadWaysToPolyRoads.get(way);
            OSMRoadNode first = (OSMRoadNode) way.getFirst();
            OSMRoadNode last = (OSMRoadNode) way.getLast();
            for (OSMRoadWay connectedWay : first.getConnections()) {
                PolyRoad otherRoad = roadWaysToPolyRoads.get(connectedWay);
                if (road != otherRoad) {
                    road.addConnectionToFirst(otherRoad);
                }
            }
            for (OSMRoadWay connectedWay : last.getConnections()) {
                PolyRoad otherRoad = roadWaysToPolyRoads.get(connectedWay);
                if (road != otherRoad) {
                    road.addConnectionToLast(otherRoad);
                }
            }
        }
    }

    private void fillPolyRoadsIntoArray() {
        //We start from 0, so that we can reference roads and use 0 as an uninitialized reference,
        // as ints are initialized to 0 by default.
        PolyRoad.initializePolyRoadRegister(roadWaysToPolyRoads.values().size() + 1);
        int i = 1;
        for (PolyRoad road : roadWaysToPolyRoads.values()) {
            polyRoadToIntegers.put(road, i);
            PolyRoad.setPolyRoadByIndex(i, road);
            road.setIndex(i);
            i++;
        }
    }

    private void createPolyRoadsFromOSMRoads() {
        for (OSMRoadWay way : osmRoads) {
            PolyRoad newRoad = new PolyRoad(way);
            roadWaysToPolyRoads.put(way, newRoad);
            drawableModel.add(way.getType(), newRoad);
        }
    }

    private List<OSMRoadWay> splitWays() {
        List<OSMRoadWay> toBeAdded = new ArrayList<>();
        for (OSMRoadWay way : osmRoads) {
            OSMRoadWay newWay = way;
            while (newWay != null) {
                OSMRoadWay oldRoad = newWay;
                newWay = newWay.splitIfNeeded();
                if (newWay != null) {
                    toBeAdded.add(newWay);
                } else if (way.getRestrictions().contains(RoadRestriction.ROUNDABOUT)) {
                    ((OSMRoadNode) oldRoad.getLast()).add(way);
                }
            }
        }
        return toBeAdded;
    }


    public void addRoad(OSMRoadWay currentWay) {
        osmRoads.add(currentWay);
    }

    public void addRoadNode(OSMRoadNode newNode) {
        roadNodes.add(newNode);
    }
}
