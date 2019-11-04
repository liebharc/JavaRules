package com.github.liebharc.JavaRules.sharedknowledge
import java.{lang, util}

import scala.collection.immutable.Map
import scala.collection.immutable.List
import collection.JavaConverters._
import com.github.liebharc.JavaRules.model.{SchoolClass, Student}

case class Snapshot(
  classes: Map[Long, SchoolClass],
  students: Map[Long, Student],
  assignedStudents: Map[Long, List[Student]],
  activeStudents: Map[Long, List[Student]],
  attendees: Map[Long, List[Student]],
  studyTimes: Map[Long, Integer],
  classesMissed: Map[Long, Integer])

class SnapshotDataStore extends DataAccess {
  private var classes = Map[Long, SchoolClass]()

  private var students = Map[Long, Student]()

  private var assignedStudents = Map[Long, List[Student]]()

  private var activeStudents = Map[Long, List[Student]]()

  private var attendees = Map[Long, List[Student]]()

  private var studyTimes = Map[Long, Integer]()

  private var classesMissed = Map[Long, Integer]()

  def createSnapshot() = Snapshot(classes, students, assignedStudents, activeStudents, attendees, studyTimes, classesMissed)

  def loadSnapshot(snapshot: Snapshot): Unit = {
    classes = snapshot.classes
    students = snapshot.students
    assignedStudents = snapshot.assignedStudents
    activeStudents = snapshot.activeStudents
    attendees = snapshot.attendees
    students = snapshot.students
    classesMissed = snapshot.classesMissed
  }

  override def getAssignedClasses(studentId: Long): util.Collection[SchoolClass] = getClassesForStudent(assignedStudents, studentId).asJavaCollection

  private def getClassesForStudent(map: Map[Long, List[Student]], studentId: Long) = map.filter(e => e._2.find(s => s.getId == studentId).isDefined).map(e => classes(e._1))

  override def getActiveStudents(classId: Long): util.Collection[Student] = activeStudents.get(classId).getOrElse(Nil).asJavaCollection

  override def getActiveStudents: util.Collection[Student] = classes.values.flatMap(classId => activeStudents.get(classId.getId).getOrElse(Nil)).toSeq.asJavaCollection

  override def getActiveClasses: util.Collection[SchoolClass] = classes.values.asJavaCollection

  override def assignStudent(schoolClass: Long, student: Long): Unit =
    assignedStudents = addToMap(assignedStudents, classes(schoolClass), students(student))

  override def unassignStudent(schoolClass: Long, student: Long): Unit =
    assignedStudents = removeFromMap(assignedStudents, classes(schoolClass), students(student))

  override def markAsAttended(schoolClass: Long, student: Long): Unit =
    attendees = addToMap(attendees, classes(schoolClass), students(student))

  override def clearAttendees(schoolClass: Long): Unit =
    attendees -= schoolClass

  override def getAttendees(schoolClass: Long): util.Collection[Student] =
    attendees.getOrElse(schoolClass, Nil).asJavaCollection

  override def markStudentAsActive(schoolClass: Long, student: Long): Unit =
    activeStudents = addToMap(activeStudents, classes(schoolClass), students(student))

  override def markStudentAsInactive(schoolClass: Long, student: Long): Unit =
    activeStudents = removeFromMap(activeStudents, classes(schoolClass), students(student))

  override def addStudyTime(student: Long, time: Int): Unit = {
    val current: Integer = studyTimes.getOrElse(student, 0)
    val updated = current + time
    studyTimes += (student -> updated)
  }

  override def getStudyTime(student: Long): Int = studyTimes.getOrElse(student, 0).asInstanceOf[Int]

  override def incrementClassesMissed(student: Long): Unit = {
    val current: Integer = classesMissed.getOrElse(student, 0)
    val updated = current + 1
    classesMissed += (student -> updated)
  }

  override def getNumberOfMissedClasses(student: Long): Int = classesMissed.getOrElse(student, 0).asInstanceOf[Int]

  private def addToMap(map: Map[Long, List[Student]], schoolClass: SchoolClass, student: Student):  Map[Long, List[Student]] = {
    val current = map.getOrElse(schoolClass.getId, Nil)
    val updated: List[Student] = student :: current
    map + (schoolClass.getId.asInstanceOf[Long] -> updated)
  }

  private def removeFromMap(map: Map[Long, List[Student]], schoolClass: SchoolClass, student: Student): Map[Long, List[Student]] = {
    val current = map.getOrElse(schoolClass.getId, Nil)
    val updated: List[Student] = current.filter(s => s.getId != student.getId)
    map + (schoolClass.getId.asInstanceOf[Long] -> updated)
  }

  override def store(schoolClass: SchoolClass): Long = {
    classes += (schoolClass.getId.asInstanceOf[Long] -> schoolClass)
    schoolClass.getId
  }

  override def store(student: Student): Long = {
    students += (student.getId -> student)
    student.getId
  }

  private def contains(map: Map[Long, List[Student]], student: Long, schoolClass: Long): Boolean =
    map.getOrElse(schoolClass, Nil).find(s => s.getId == student).isDefined

  override def isAssigned(student: lang.Long, schoolClass: lang.Long): Boolean = contains(assignedStudents, student, schoolClass)

  override def isActive(student: lang.Long, schoolClass: lang.Long): Boolean = contains(activeStudents, student, schoolClass)
}
