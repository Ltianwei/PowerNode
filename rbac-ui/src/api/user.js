import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    params:data
  })
}

export function getInfo(token) {
  return request({
    url: '/getUserInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'get'
  })
}

//得到验证码
export function getCaptcha(){
  return request({
    url: `/captcha/getCaptcha`,
    method: 'get'
  })
}
