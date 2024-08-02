import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const nameKey = 'userName'
const userIdKey = 'userId'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

//设置userName
export function getName() {
  return Cookies.get(nameKey)
}

export function setName(token) {
  return Cookies.set(nameKey, token)
}

export function removeName() {
  return Cookies.remove(nameKey)
}

//设置userId
export function getUserIdKey() {
  return Cookies.get(userIdKey)
}

export function setUserIdKey(token) {
  return Cookies.set(userIdKey, token)
}

export function removeUserIdKey() {
  return Cookies.remove(userIdKey)
}
