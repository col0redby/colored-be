package com.colored.be.models.tables

import java.time.LocalDate

case class UsersTable (
  id: Int,
  username: String,
  email: String,
  country: String,
  birthday: LocalDate
)
