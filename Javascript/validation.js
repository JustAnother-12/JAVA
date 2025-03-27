export const isEmpty = (value) => {
  return (
    String(value).trim().length === 0 || value === null || value === undefined
  );
};

export const isEmail = (value) => {
  const regex =
    /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  return regex.test(String(value));
};

export const isNumber = (value) => {
  return Number(String(value));
};

export const isValidProductID = (value) => {
  const regex = /^(BK)\d{4}$/g;
  return regex.test(String(value));
};

export const isValidUserID = (value) => {
  const regex = /^KH\d{3,}$/;
  return regex.test(String(value));
};

export const isExistedProductID = (products, productID) => {
  return products.some((product) => product.productID === productID);
};

export const isExistedUserID = (users, userID) => {
  return users?.some((user) => user.userID === userID);
};

export const isExistedUserEmail = (users, email) => {
  return users?.some((user) => user.email === email);
};

export const isConfirmPassword = (password, confirmPassword) => {
  return password === confirmPassword;
};
