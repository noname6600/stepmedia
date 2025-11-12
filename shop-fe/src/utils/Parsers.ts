export const intParam = (value: string | null, defaultValue?: number) => {
  if (value === null) return defaultValue;
  return parseInt(value, 10);
};

export const strParam = (value: string | null, defaultValue?: string) => {
  if (value === null) return defaultValue;
  return value;
};

export const floatParam = (value: string | null, defaultValue?: number) => {
  if (value === null) return defaultValue;
  return parseFloat(value);
};

export const typeParam = <T extends string>(value: any, defaultValue?: T): T | undefined => {
  if (value === null) return defaultValue;
  return value;
};

export const currency = (value: number | undefined) => {
  return value && value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

export const parseParams = (params: URLSearchParams) =>
  [...params.entries()].reduce((acc: any, tuple) => {
    const [key, val] = tuple;
    if (acc.hasOwnProperty(key)) {
      if (Array.isArray(acc[key])) {
        acc[key] = [...acc[key], val];
      } else {
        acc[key] = [acc[key], val];
      }
    } else {
      acc[key] = val;
    }
    return acc;
  }, {});

export const setNativeValue = (element: HTMLInputElement, value: string = '') => {
  const valueSetter = Object.getOwnPropertyDescriptor(element, 'value')?.set;
  const prototype = Object.getPrototypeOf(element);
  const prototypeValueSetter = Object.getOwnPropertyDescriptor(prototype, 'value')?.set;

  if (valueSetter && valueSetter !== prototypeValueSetter) {
    prototypeValueSetter?.call(element, value);
  } else {
    valueSetter?.call(element, value);
  }
};
