import { useSelector } from 'react-redux';
import TipTitle from './Tips/TipTitle';
import NextBtn from './button/NextBtn';

export default function Title({ isSelected, setisSelected }) {
  const tipboxNum = useSelector((state) => {
    return state.tipbox.value; // store 안에 reducer가 저장되어 있는 Slice의 이름
  });

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">Title</div>

      <div className="my-1 text-xs">
        Be specific and imagine you’re asking a question to another person.
      </div>

      <input
        className="my-1 py-1.5 pl-2 text-sm bg-white border-2 border-solid border-gray rounded-md"
        placeholder="e.g. Is there an R function for finding the index of an element in a vector?"
        onFocus={() => {
          setisSelected(1);
        }}
      ></input>

      {tipboxNum === 1 && <NextBtn />}
      {isSelected === 1 && <TipTitle />}
    </div>
  );
}
