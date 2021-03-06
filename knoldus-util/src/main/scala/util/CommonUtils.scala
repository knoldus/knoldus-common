/**
 * Copyright (C) Knoldus Software LLP. <http://www.knoldus.com>
 */

package util

/**
 * This object contains common utility functions.
 * These functions are generally used in many projects.
 * For Example:- Sorting list of list index wise,
 * preserve ordering of collection while doing groupBy etc.
 *
 * @author ayush
 */

object CommonUtils {

  /**
   * Type-agnostic solution to do Cross Product of all sub list.
   *
   * Example:-
   * scala> val input = List(List(1,2,3),List("a","b"),List("@"))
   * input: List[List[Any]] = List(List(1, 2, 3), List(a, b), List(@))
   *
   * scala> crossProduct(input)
   * res0: List[List[Any]] = List(List(1, a, @), List(2, a, @), List(3, a, @), List(1, b, @), List(2, b, @), List(3, b, @))
   *
   * @param elements: List of sub lists of any type
   * @return List of crossed sub list
   */
  def crossProduct[A](elements: List[List[A]]): List[List[A]] =
    elements match {
      case element :: nextElement :: Nil => element.map(a => nextElement.map(b => List(a, b))).flatten
      case element :: currentElement :: nextElement =>
        crossProduct(currentElement :: nextElement).map(li => element.map(a => a :: li)).flatten
      case element :: Nil => element.map(a => List(List(a))).flatten
      case _ => elements
    }

  /**
   * Solution to sort sub list by list of indexes.
   *
   * Example:-
   * scala> val input = List(List(5,"a",7),List(7,"b",8),List(4,"a",3),List(1,"b",2),List(3,"c",8))
   * input: List[List[Any]] = List(List(5, a, 7), List(7, b, 8), List(4, a, 3), List(1, b, 2), List(3, c, 8))
   *
   * scala> sortSubListByIndexes(input,List(1,0))
   * res5: List[List[_]] = List(List(4, a, 3), List(5, a, 7), List(1, b, 2), List(7, b, 8), List(3, c, 8))
   *
   * If there is no index, default sorting would be applied on List
   * @param elements: List of sub lists of any type
   * @param indexes: List of indexes, on which sorting to be done
   * @return List of sorted sub list by indexes
   */

  def sortSubListByIndexes(elements: List[List[_]], indexes: List[Int] = Nil) = {
    elements sortBy { element => (indexes map { element(_) }).mkString("") + element.mkString("") }
  }

  /**
   * Compare Any Data Type Value
   *
   * Example:-
   * scala> compare(5,"lt")
   * res23: Int => Boolean = <function1>
   *
   * scala> val isLessThan5 = compare(5,"lt")
   * isLessThan5: Int => Boolean = <function1>
   *
   * scala> val input = List(1,2,3,4,5,6,7,8)
   * input: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8)
   *
   * scala> input filter (isLessThan5(_))
   * res24: List[Int] = List(1, 2, 3, 4)
   *
   * scala> val format = new java.text.SimpleDateFormat("dd-MM-yyyy")
   * format: java.text.SimpleDateFormat = java.text.SimpleDateFormat@9586200
   * 
   * scala> val input = List(format.parse("21-03-2011"),format.parse("23-02-1911"),format.parse("21-04-2011"),format.parse("01-05-2011"))
   * input: List[java.util.Date] = List(Mon Mar 21 00:00:00 IST 2011, Thu Feb 23 00:00:00 IST 1911, Thu Apr 21 00:00:00 IST 2011, Sun May 01 00:00:00 IST 2011)
   *
   * scala> input filter (compare(format.parse("21-03-2011"), "gt")(_))
   * res28: List[java.util.Date] = List(Thu Apr 21 00:00:00 IST 2011, Sun May 01 00:00:00 IST 2011)
   *
   * @param value: That is to be compared.
   * @param condition: Matching condition. Matching condition could be any of "lt","lte","gt","gte" or "eq". 
   * If there is no condition, default condition is "eq".
   * @return [dataType]=> Boolean = <function1>
   */
  def compare[A](value: A, condition: String = "eq")(implicit ord: Ordering[A]) = {
    ((condition.equals("lt")), (condition.equals("lte")),
      (condition.equals("gt")), (condition.equals("gte")),
      (condition.equals("eq"))) match {
        case (true, false, false, false, false) => ((ord.lt _).curried)(_: A)(value)
        case (false, true, false, false, false) => ((ord.lteq _).curried)(_: A)(value)
        case (false, false, true, false, false) => ((ord.gt _).curried)(_: A)(value)
        case (false, false, false, true, false) => ((ord.gteq _).curried)(_: A)(value)
        case (_, _, _, _, _) => ((ord.equiv _).curried)(_: A)(value)
      }
  }

}
