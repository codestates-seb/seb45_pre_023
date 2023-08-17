import TipTitle from './Tips/TipTitle';
import NextBtn from './button/NextBtn';
import { useSelector, useDispatch } from 'react-redux';
import { title } from '../../redux/createSlice/AskSlice';
import { tipbox } from '../../redux/createSlice/TipboxSlice';

export default function Title() {
  const dispatch = useDispatch();
  const Next = useSelector((state) => state.tipbox.position);
  const tipboxName = useSelector((state) => state.tipbox.tipboxName);
  const InputValue = useSelector((state) => state.ask.value);
  const ErrorMsg = useSelector((state) => state.ask.errmsg);

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">Title</div>

      <div className="my-1 text-xs">
        Be specific and imagine you’re asking a question to another person.
      </div>

      <input
        className="my-1 py-1.5 pl-2 text-sm bg-white border-2 border-solid border-gray rounded-md"
        placeholder="e.g. Is there an R function for finding the index of an element in a vector?"
        value={InputValue.title}
        onChange={(e) => {
          dispatch(title(e.target.value));
        }}
        onFocus={() => {
          dispatch(tipbox('title'));
        }}
      />

      <div className='my-1 text-xs text-red-500'>{ErrorMsg.title}</div>

      {Next === 1 && <NextBtn />}
      {tipboxName === 'title' && <TipTitle />}
    </div>
  );
}
