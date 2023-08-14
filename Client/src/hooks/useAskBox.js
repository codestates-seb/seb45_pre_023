import { useState } from 'react';

export default function useAskBox() {
  const [isNum, setisNum] = useState(1);

  const up = () => {
    setisNum(isNum + 1);
    console.log(isNum)
  };

  return {isNum, up};
}
