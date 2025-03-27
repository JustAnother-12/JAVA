export const convertBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
    reader.readAsDataURL(file);
  });
};

export const getItemFromLocalStorage = (key) => {
  const data = localStorage.getItem(key);
  try {
    return data ? JSON.parse(data) : []; // Parse the data if it exists, otherwise return an empty array
  } catch (error) {
    console.error(`Error parsing data from localStorage key "${key}":`, error);
    return [];
  }
};

export const setItemFromLocalStorage = (key, value) => {
  localStorage.setItem(key, JSON.stringify(value));
};
