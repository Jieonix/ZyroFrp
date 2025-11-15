export function validateToken(router, token, isAdmin = false) {
  if (!token) {
    redirectToLogin(router, isAdmin);
    return false;
  }

  try {
    const decodedToken = decodeJWT(token);
    const expirationTime = decodedToken.exp * 1000;
    const currentTime = Date.now();

    if (currentTime > expirationTime) {
      redirectToLogin(router, isAdmin);
      return false;
    }
  } catch (error) {
    console.error("Token 解码失败：", error);
    redirectToLogin(router, isAdmin);
    return false;
  }

  return true;
}

export function decodeJWT(token) {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}

export function redirectToLogin(router, isAdmin) {
  const loginPage = isAdmin ? 'Admin_Login' : 'Login';
  router.push({ name: loginPage });
}
