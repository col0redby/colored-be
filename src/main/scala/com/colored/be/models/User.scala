package com.colored.be.models

import java.time.LocalDate

final case class User(
    id: Int,
    username: String,
    email: String,
    country: String,
    birthday: java.time.LocalDate
)

object User {

  /*
  Just to emulate user while we have not authentication functionality.
   */
  def mock: User =
    User(0, "nkharitonov", "a@a.a", "Belarus", LocalDate.of(1999, 1, 16))
}
