import { useSelector } from 'react-redux';
import NextBtn from './button/NextBtn';
import TipDetail from './Tips/TipDetail';

export default function DetailProbelm({ isSelected, setisSelected }) {
  const tipboxNum = useSelector((state) => {
    return state.tipbox.value; // store 안에 reducer가 저장되어 있는 Slice의 이름
  });

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">What are the details of your problem?</div>

      <div className="my-1 text-xs">
        Introduce the problem and expand on what you put in the title. Minimum
        20 characters.
      </div>

      <textarea
        className="my-1 py-1.5 pl-2 h-60 text-sm bg-white border-2 border-solid border-gray rounded-md"
        onFocus={() => {
          setisSelected(2);
        }}
      ></textarea>

      {tipboxNum === 2 && <NextBtn />}
      {isSelected === 2 && <TipDetail />}
    </div>
  );
}
