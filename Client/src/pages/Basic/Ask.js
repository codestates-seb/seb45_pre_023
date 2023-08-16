import Discription from '../../components/Ask/Discription';
import Title from '../../components/Ask/Title';
import DetailProbelm from '../../components/Ask/DetailProbelm';
import Try from '../../components/Ask/Try';
import Tags from '../../components/Ask/Tags';
import Review from '../../components/Ask/Review';
import DiscardBtn from '../../components/Ask/button/DiscardBtn';
import { useState } from 'react';

export default function Ask() {
  const [isSelected, setisSelected] = useState(false);
  const [isNum, setisNum] = useState(1);

  const NextNum = () => {
    setisNum(isNum + 1);
    console.log(isNum)
  };


  return (
    <div className="flex flex-row min-w-full bg-gray-100">
      <div className="grow-2"></div>
      <div className="grow-5 flex flex-col">
        <div className="my-11 font-semibold text-2xl">
          Ask a public question
        </div>
        <Discription />
        <Title isSelected={isSelected} setisSelected={setisSelected} />
        <DetailProbelm isSelected={isSelected} setisSelected={setisSelected} />
        <Try isSelected={isSelected} setisSelected={setisSelected} />
        <Tags isSelected={isSelected} setisSelected={setisSelected} />
        <Review isSelected={isSelected} setisSelected={setisSelected} />
        <DiscardBtn />
      </div>
    </div>
  );
}
