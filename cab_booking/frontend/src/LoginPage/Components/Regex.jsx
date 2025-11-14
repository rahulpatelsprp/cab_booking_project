export const validName = new RegExp("^[a-zA-Z].{2,}$");
export const validEmail = new RegExp(
  "^[a-zA-Z0-9._:$!%-]+@[a-zA-Z.-]+.[a-zA-Z]$"
);
export const validPassword = new RegExp("^(?=.*?[A-Za-z])(?=.*?[0-9]).{5,}$");
export const validMob = new RegExp("^\\d{10}$");
export const validLnumber = new RegExp("^[a-zA-Z]{2}[0-9]{13}$");
