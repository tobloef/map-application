package bfst19.danmarkskort.model.routePlanning;

import bfst19.danmarkskort.exceptions.InvalidUserInputException;
import bfst19.danmarkskort.model.drawableModel.Rectangle;
import bfst19.danmarkskort.model.drawables.PolyRoad;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Route extends ArrayList<PolyRoad> {

	public List<String> getTextDescription() {
        if (size() == 0) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        double summedLength = get(0).getRealLength();
        result.add("Start on " + get(0).getStreetNameOrDefault());
        for (int i = 1; i < this.size(); i++) {
            PolyRoad road = get(i);
            summedLength += road.getRealLength();
            if (get(i - 1).getStreetNameOrDefault().equals(road.getStreetNameOrDefault())) {
                continue;
            }
            String description = makeDescription(summedLength, i, road);
            result.add(description);
            summedLength = 0;
        }
        result.add("You will then arrive at your destination");
        return result;
    }

    private String makeDescription(double summedLength, int i, PolyRoad road) {
        String description = "";
        description += "Drive ";
        description += getLengthDescription(summedLength);
        description += getDirectionDescription(get(i - 1), road);
        description += road.getStreetNameOrDefault();
        return description;
    }

    private String getLengthDescription(double summedLength) {
        String unit = "km";
        summedLength *= 110;
        if (summedLength < 1) {
            summedLength *= 1000;
            unit = "m";
            return "" + (int) summedLength + " " + unit;
        }
        return "" + String.format("%.1f", summedLength) + " " + unit;
    }

    private String getDirectionDescription(PolyRoad actualLast, PolyRoad road) {
        double degree = getDegree(actualLast, road);
        int direction = interpretDirection(degree);
        switch (direction) {
            case 1: {
                return " and turn right onto ";
            }
            case 0: {
                return " and continue forward onto ";
            }
            case -1: {
                return " and turn left onto ";
            }
        }
        throw new RuntimeException("interpretDirection returned a number that isn't -1, 0 or 1");
    }


	private double getDegree(PolyRoad last, PolyRoad current) {
        double lastDegree = last.getDegree(current);
        double firstDegree = current.getDegree(last);
        return clampAngle(lastDegree - firstDegree + 180); //the 180 is because directly ahead is 180, while one degree
        // left of that is -179, so we flip the angle around so it's easier to work with
    }

    private int interpretDirection(double degree) {
        double rightBound = 45;
        double leftBound = -45;
        if (degree > rightBound) {
            return 1; //if you are going exactly backwards, you are technically turning left, since backwards would be 180 degrees.
        }
        if (degree > leftBound) {
            return 0;
        }
        return -1;
    }

    private double clampAngle(double angle) {
        if (angle > 180) {
            angle -= 360;
        } else if (angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    public void printToFile(File file) {
        if (file == null) {
            return;
        }
        try {
            if (isEmpty()) {
                throw new InvalidUserInputException("Please select a route.");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (String string : getTextDescription()) {
                bufferedWriter.write(string + String.format("%n"));
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidUserInputException e) {
            makeAlert(e);
        }
    }

    private void makeAlert(InvalidUserInputException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                e.getMessage(),
                ButtonType.CLOSE);
        alert.setTitle("Error: Wrong input");
        alert.setHeaderText("Error: Wrong input");
        alert.show();
    }

    public String getSuggestedFileName() {
        return get(0).getStreetNameOrDefault() + "_" + get(size() - 1).getStreetNameOrDefault() + ".txt";
    }

    public Rectangle getBoundingBox(){
        Rectangle boundingBox = new Rectangle(get(0).getMinimumBoundingRectangle());
        for (PolyRoad road : this){
            boundingBox.growToEncompass(road.getMinimumBoundingRectangle());
        }
        return boundingBox;
    }
}
