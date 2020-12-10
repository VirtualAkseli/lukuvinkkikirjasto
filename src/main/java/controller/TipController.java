package controller;

import tiplogic.*;

import java.util.ArrayList;
import java.util.List;

import database.*;
import static utilities.MappingUtils.toMap;

public class TipController {
    private List<Tip> tips;
    private List<Tag> allTags;
    private List<Course> allCourses;
    private Dao<Tip, Long> tipDao;
    private Dao<Course, Long> courseDao;
    private Dao<Tag, Long> tagDao;

    public TipController(Dao<Tip, Long> tipDao) {
        this.tipDao = tipDao;
        this.tips = tipDao.list();
        this.allTags = new ArrayList<>();
        this.allCourses = new ArrayList<>();
    }
    public TipController(Dao<Tip, Long> tipDao, Dao<Course, Long> courseDao, Dao<Tag, Long> tagDao) {
        this.tipDao = tipDao;
        this.courseDao = courseDao;
        this.tagDao = tagDao;
        this.tips = tipDao.list();
        this.allTags = tagDao.list();
        this.allCourses = courseDao.list();
    }
        //method recieves an array with tip data asked from the user
    //the method presumes the array had been created correctly in UI
    //strings in indexes: 0 tip type, 1 author, 2 tip name, 3 identifyer, 4 url, 5 comments
    public void addTip(String[] data) {
        Tip tip = new Tip(data[1], data[2]);
        Long id = tipDao.create(tip);
        tip.setId(id);
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
            Long id = tagDao.create(newT);
            newT.setId(id);
            tipTags.add(newT);
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
            Long id = courseDao.create(newC);
            newC.setId(id);
            tipCourses.add(newC);
        }
        tip.setCourses(tipCourses);
    }

    public List<Tip> list() {
        return this.tips;
    }

    public List<Tip> searchWithType(String type) {
        ArrayList<Tip> results = new ArrayList<>();
        for (Tip tip: tips) if (tip.getTipType().equals(type)) results.add(tip);
        return results;
    }

    public List<Tip> searchWithWord(String keyword) {
        List<Tip> t = new ArrayList<>();
        for (Tip tip: tipDao.getByValue(toMap("tip_name", keyword))) t.add(tip);
        for (Tip tip: tipDao.getByValue(toMap("author", keyword))) t.add(tip);
        return t;
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
