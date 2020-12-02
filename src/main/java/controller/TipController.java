package controller;

import java.util.*;

import tietokanta.*;
import vinkkilogic.*;

public class TipController {
    private ArrayList<Tip> tips;
    private ArrayList<Tag> allTags;
    private ArrayList<Course> allCourses;

    public TipController() {
        this.tips = new ArrayList<>();
        this.allTags = new ArrayList<>();
        this.allCourses = new ArrayList<>();
    }
    //method recieves an array with tip data asked from the user
    //the method presumes the array had been created correctly in UI
    //strings in indexes: 0 tip type, 1 author, 2 tip name, 3 identifyer, 4 url, 5 comments
    public void addTip(String[] data) {
        Tip tip = new Tip(data[1], data[2]);
        tip.setTipType(data[0]);
        tip.setIdentifier(data[3]);
        tip.setUrl(data[4]);
//        addTags(tip, separate(data[X]));
//        addCourses(tip, separate(data[X]));
        tip.setComments(data[5]);
        tips.add(tip);
    }

    private void addTags(Tip tip, List<String> t) {
        ArrayList<Tag> tipTags = new ArrayList<>();
        for (String newTag: t) {
            for (Tag tag: allTags) {
                if (tag.getTagName().equals(newTag)) {
                    tipTags.add(tag);
                    break;
                }
            }
            Tag newT = new Tag((long) 12345, newTag);
            tipTags.add(newT);
            //add new tag to the database
        }
        tip.setTags(tipTags);
    }
    private void addCourses(Tip tip, List<String> c) {
        ArrayList<Course> tipCourses = new ArrayList<>();
        for (String newCourse: c) {
            for (Course course: allCourses) {
                if (course.getCourseName().equals(newCourse)) {
                    tipCourses.add(course);
                    break;
                }
            }
            Course newC = new Course((long) 54321, newCourse);
            tipCourses.add(newC);
            //add new course to the database
        }
        tip.setCourses(tipCourses);
    }

    public List<Tip> getTips() {
        return this.tips;
    }

    public List<Tip> searchWithType(String type) {
        ArrayList<Tip> results = new ArrayList<>();
        for (Tip tip: tips) if (tip.getTipType().equals(type)) results.add(tip);
        return results;
    }

    public List<Tip> searchWithTag(Tag tag) {
        ArrayList<Tip> results = new ArrayList<>();
        for (Tip tip: tips) if (tip.getTags().contains(tag)) results.add(tip);
        return results;
    }

    //assist method to split the strings at a comma
    private List<String> separate(String dataArrayList) {
        ArrayList<String> data = new ArrayList<>();
        if (!dataArrayList.contains(",")) {
            data.add(dataArrayList);
            return data;
        }
        String[] separatedStrings = dataArrayList.split(",");
        for (String pieceOfData: separatedStrings) data.add(pieceOfData.trim());
        return data;
    }
}
