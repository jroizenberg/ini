package com.institucion.fm.lang;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Day  implements Comparable<Day> , Serializable {
	private static final long serialVersionUID = 1L;
  private Calendar calendar_ = Calendar.getInstance();

  public Day(int year, int month, int dayOfMonth){
    initialize(year, month, dayOfMonth);
  }

  public Day(int year, int dayOfYear)
  {
    initialize(year, Calendar.JANUARY, 1);
    calendar_.set(Calendar.DAY_OF_YEAR, dayOfYear);
  }

  public Day()
  {
    // Now (in the currenct locale of the client machine)
    Calendar calendar = Calendar.getInstance();

    // Prune time part
    initialize(calendar.get(Calendar.YEAR),
               calendar.get(Calendar.MONTH),
               calendar.get(Calendar.DAY_OF_MONTH));
  }

  public Day(Calendar calendar)
  {
    if (calendar == null)
      throw new IllegalArgumentException("calendar cannot be null");

    initialize(calendar);
  }

  public Day(Date date)
  {
    if (date == null)
      throw new IllegalArgumentException("dat cannot be null");

    // Create a calendar based on given date
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

//    initialize(calendar);
//    // Extract date values and use these only
    initialize(calendar.get(Calendar.YEAR),
               calendar.get(Calendar.MONTH),
               calendar.get(Calendar.DAY_OF_MONTH),
               calendar.get(Calendar.HOUR_OF_DAY),
               calendar.get(Calendar.MINUTE),
               calendar.get(Calendar.SECOND),
               calendar.get(Calendar.MILLISECOND));
  }
  
  public Day(long time)
  {
    this(new Date(time));
  }
  
  public Day(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");

    initialize(day.getYear(), day.getMonth(), day.getDayOfMonth());
  }

  private void initialize(int year, int month, int dayOfMonth)
  {
    calendar_.set(Calendar.YEAR, year);
    calendar_.set(Calendar.MONTH, month);
    calendar_.set(Calendar.DAY_OF_MONTH, dayOfMonth);

    // Prune the time component
    calendar_.set(Calendar.HOUR_OF_DAY, 0);
    calendar_.set(Calendar.MINUTE, 0);
    calendar_.set(Calendar.SECOND, 0);
    calendar_.set(Calendar.MILLISECOND, 0);
  }
  private void initialize(int year, int month, int dayOfMonth, int hh, int mm, int ss, int ml)
  {
    calendar_.set(Calendar.YEAR, year);
    calendar_.set(Calendar.MONTH, month);
    calendar_.set(Calendar.DAY_OF_MONTH, dayOfMonth);

    // Prune the time component
    calendar_.set(Calendar.HOUR_OF_DAY, hh);
    calendar_.set(Calendar.MINUTE, mm);
    calendar_.set(Calendar.SECOND, ss);
    calendar_.set(Calendar.MILLISECOND, ml);
  }
  
  private void initialize(Calendar calendar)
  {
    calendar_= calendar;
  }

  public static Day today()
  {
    return new Day();
  }

  public Calendar getCalendar()
  {
    return (Calendar) calendar_.clone();
  }

  public Date getDate()
  {
    return getCalendar().getTime();
  }

  public int compareTo(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");

    return calendar_.getTime().compareTo(day.calendar_.getTime());
  }

  public boolean isAfter(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");

    return calendar_.after(day.calendar_);
  }

  public boolean isBefore(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");

    return calendar_.before(day.calendar_);
  }

  @Override
  public boolean equals(Object object) {
    Day day = (Day) object;

    if (day == null)
      throw new IllegalArgumentException("day cannot be null");
    
    if(day.getDayOfMonth() == this.getDayOfMonth() &&
    		day.getMonthNo() == this.getMonthNo() &&
    		day.getYear() == this.getYear())
    	return true;
    
    return false;
  }

  @Override
  public int hashCode()
  {
    return calendar_.hashCode();
  }

  public int getYear()
  {
    return calendar_.get(Calendar.YEAR);
  }

  public int getMonth()
  {
    return calendar_.get(Calendar.MONTH);
  }

  public int getMonthNo()
  {
    // It is tempting to return getMonth() + 1 but this is conceptually
    // wrong, as Calendar month is an enumeration and the values are tags
    // only and can be anything.
    switch (getMonth()) {
      case Calendar.JANUARY   : return 1;
      case Calendar.FEBRUARY  : return 2;
      case Calendar.MARCH     : return 3;
      case Calendar.APRIL     : return 4;
      case Calendar.MAY       : return 5;
      case Calendar.JUNE      : return 6;
      case Calendar.JULY      : return 7;
      case Calendar.AUGUST    : return 8;
      case Calendar.SEPTEMBER : return 9;
      case Calendar.OCTOBER   : return 10;
      case Calendar.NOVEMBER  : return 11;
      case Calendar.DECEMBER  : return 12;
      default :
        assert false : "Invalid mongth: " + getMonth();
    }

    // This will never happen
    return 0;
  }

  public int getDayOfMonth()
  {
    return calendar_.get(Calendar.DAY_OF_MONTH);
  }

  public int getDayOfYear()
  {
    return calendar_.get(Calendar.DAY_OF_YEAR);
  }
  
  public int getDayOfWeek()
  {
    return calendar_.get(Calendar.DAY_OF_WEEK);
  }
  
  public int getDayNumberOfWeek()
  {
    return getDayOfWeek() == Calendar.SUNDAY ?
                           7 : getDayOfWeek() - Calendar.SUNDAY;
  }

  public int getWeekOfYear()
  {
    return calendar_.get(Calendar.WEEK_OF_YEAR);
  }

  public Day addDays(int nDays)
  {
    // Create a clone
    Calendar calendar = (Calendar) calendar_.clone();

    // Add/remove the specified number of days
    calendar.add(Calendar.DAY_OF_MONTH, nDays);

    // Return new instance
    return new Day(calendar);
  }

  public Day subtractDays(int nDays)
  {
    return addDays(-nDays);
  }

  public Day addMonths(int nMonths)
  {
    // Create a clone
    Calendar calendar = (Calendar) calendar_.clone();

    // Add/remove the specified number of days
    calendar.add(Calendar.MONTH, nMonths);

    // Return new instance
    return new Day(calendar);
  }

  public Day subtractMonths(int nMonths)
  {
    return addMonths(-nMonths);
  }

  public Day addYears(int nYears)
  {
    // Create a clone
    Calendar calendar = (Calendar) calendar_.clone();

    // Add/remove the specified number of days
    calendar.add(Calendar.YEAR, nYears);

    // Return new instance
    return new Day(calendar);
  }

  public Day subtractYears(int nYears)
  {
    return addYears(-nYears);
  }

  public int getDaysInYear()
  {
    return calendar_.getActualMaximum(Calendar.DAY_OF_YEAR);
  }

  public boolean isLeapYear()
  {
    return getDaysInYear() == calendar_.getMaximum(Calendar.DAY_OF_YEAR);
  }

  public static boolean isLeapYear(int year)
  {
    return (new Day(year, Calendar.JANUARY, 1)).isLeapYear();
  }

  public int getDaysInMonth()
  {
    return calendar_.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  public String getDayName()
  {
    switch (getDayOfWeek()) {
      case Calendar.MONDAY    : return "Monday";
      case Calendar.TUESDAY   : return "Tuesday";
      case Calendar.WEDNESDAY : return "Wednesday";
      case Calendar.THURSDAY  : return "Thursday";
      case Calendar.FRIDAY    : return "Friday";
      case Calendar.SATURDAY  : return "Saturday";
      case Calendar.SUNDAY    : return "Sunday";
      default :
        assert false : "Invalid day of week: " + getDayOfWeek();
    }

    // This will never happen
    return null;
  }

  public int daysBetween(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");

    long millisBetween = Math.abs(calendar_.getTime().getTime() -
                                  day.calendar_.getTime().getTime());
    return (int) Math.round(millisBetween / (1000 * 60 * 60 * 24));
  }

  public static Day getNthOfMonth(int n, int dayOfWeek, int month, int year)
  {
    // Validate the dayOfWeek argument
    if (dayOfWeek < 0 || dayOfWeek > 6)
      throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);

    Day first = new Day(year, month, 1);

    int offset = dayOfWeek - first.getDayOfWeek();
    if (offset < 0) offset = 7 + offset;

    int dayNo = (n - 1) * 7 + offset + 1;

    return dayNo > first.getDaysInMonth() ? null : new Day(year, month, dayNo);
  }
  
  public static Day getNthOfMonth(Day day, int n)
  {
	  int dayOfWeek = day.getDayOfWeek();
	  int month = day.getMonth();
	  int year = day.getYear();
	  
    // Validate the dayOfWeek argument
    if (dayOfWeek < 0 || dayOfWeek > 6)
      throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);

    Day first = new Day(year, month, 1);

    int offset = dayOfWeek - first.getDayOfWeek();
    if (offset < 0) offset = 7 + offset;

    int dayNo = (n - 1) * 7 + offset + 1;

    return dayNo > first.getDaysInMonth() ? null : changeDayDate(day, year, month, dayNo);
  }

  private static Day changeDayDate(Day dayToChange, int year, int month, int dayNo){
	  dayToChange.calendar_.set(Calendar.DAY_OF_WEEK, dayNo);
	  dayToChange.calendar_.set(Calendar.MONTH, month);
	  dayToChange.calendar_.set(Calendar.YEAR, year);
	  return dayToChange;
  }

  public static Day getFirstOfMonth(int dayOfWeek, int month, int year)
  {
    return Day.getNthOfMonth(1, dayOfWeek, month, year);
  }

  public static Day getTHOfMonth(int dayOfWeek, int month, int year, int th)
  {
    return Day.getNthOfMonth(th, dayOfWeek, month, year);
  }
  
  public static int getTHOfMonth(Day day)
  {
	  for (int i = 1; i < 6; i++) {
		  Day dayToReturn = Day.getNthOfMonth(i, day.getDayOfWeek(), day.getMonth(), day.getYear());
		  if(dayToReturn!= null && day.equals(dayToReturn))
			  return i;
	  }
	  return 0;
  }

  public static Day getLastOfMonth(int dayOfWeek, int month, int year)
  {
    Day day = Day.getNthOfMonth(5, dayOfWeek, month, year);
    return day != null ? day : Day.getNthOfMonth(4, dayOfWeek, month, year);
  }

  @Override
  public String toString()
  {
    StringBuffer s = new StringBuffer();

    if (getDayOfMonth() < 10)
      s.append('0');
    s.append(getDayOfMonth());
    s.append('/');
    if (getMonth() < 9)
      s.append('0');
    s.append(getMonth() + 1);
    s.append('-');
    s.append(getYear());
    s.append(" ");
    s.append(getDayName());

    return s.toString();
  }

  public static Date getTHDateNextMonth(Date dateFrom, int thDelta, int monthDelta){
	  Day dayNow = new Day(dateFrom);
	  int th = Day.getTHOfMonth(dayNow);
	  Day day = Day.getTHOfMonth(dayNow.getDayOfWeek(),dayNow.getMonth()+monthDelta, dayNow.getYear(), th + thDelta);
	 
	  if(day==null)
		  return null;
	  
	  Date dateConverted = day.getDate();
	  
	  Calendar calendarFrom = new GregorianCalendar();
	  calendarFrom.setTime(dateFrom);
	  
	  Calendar calendarConverted = Calendar.getInstance();
	  calendarConverted.setTime(dateConverted);
	  
	  calendarConverted.set(Calendar.HOUR_OF_DAY, calendarFrom.get(Calendar.HOUR_OF_DAY));
	  calendarConverted.set(Calendar.MINUTE, calendarFrom.get(Calendar.MINUTE));
	  calendarConverted.set(Calendar.SECOND, calendarFrom.get(Calendar.SECOND));
	  calendarConverted.set(Calendar.MILLISECOND, calendarFrom.get(Calendar.MILLISECOND));
	  
	  return calendarConverted.getTime();
  }
  
  public static Date getTHDateNextMonth(Date dateFrom){
	  Day dayNow = new Day(dateFrom);
	  int th = Day.getTHOfMonth(dayNow);
	  Day day = Day.getTHOfMonth(dayNow.getDayOfWeek(),dayNow.getMonth()+1, dayNow.getYear(), th);
	 
	  if(day==null)
		  return null;
	  
	  Date dateConverted = day.getDate();
	  
	  Calendar calendarFrom = new GregorianCalendar();
	  calendarFrom.setTime(dateFrom);
	  
	  Calendar calendarConverted = Calendar.getInstance();
	  calendarConverted.setTime(dateConverted);
	  
	  calendarConverted.set(Calendar.HOUR_OF_DAY, calendarFrom.get(Calendar.HOUR_OF_DAY));
	  calendarConverted.set(Calendar.MINUTE, calendarFrom.get(Calendar.MINUTE));
	  calendarConverted.set(Calendar.SECOND, calendarFrom.get(Calendar.SECOND));
	  calendarConverted.set(Calendar.MILLISECOND, calendarFrom.get(Calendar.MILLISECOND));
	  
	  return calendarConverted.getTime();
  }

}


