import { useSelector } from 'react-redux';
import NextBtn from './button/NextBtn';
import TipTry from './Tips/TipTry';

export default function Try({ isSelected, setisSelected }) {
  const tipboxNum = useSelector((state) => {
    return state.tipbox.value; // store 안에 reducer가 저장되어 있는 Slice의 이름
  });

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">
        What did you try and what were you expecting?
      </div>

      <div className="my-1 text-xs">
        Describe what you tried, what you expected to happen, and what actually
        resulted. Minimum 20 characters.
      </div>

      <textarea
        className="my-1 py-1.5 pl-2 h-60 text-sm bg-white border-2 border-solid border-gray rounded-md"
        onFocus={() => {
          setisSelected(3);
        }}
      ></textarea>

      {tipboxNum === 3 ? <NextBtn /> : null}
      {isSelected === 3 && <TipTry />}
    </div>
  );
}
