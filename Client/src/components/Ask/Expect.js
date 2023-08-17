import NextBtn from './button/NextBtn';
import TipExpect from './Tips/TipExpect';
import { useSelector, useDispatch } from 'react-redux';
import { expect } from '../../redux/createSlice/AskSlice';
import { tipbox } from '../../redux/createSlice/TipboxSlice';

export default function Try() {
  const dispatch = useDispatch();
  const Next = useSelector((state) => state.tipbox.position);
  const tipboxName = useSelector((state) => state.tipbox.tipboxName);
  const InputValue = useSelector((state) => state.ask.value);
  const ErrorMsg = useSelector((state) => state.ask.errmsg);

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
        value={InputValue.expect}
        onChange={(e) => {
          dispatch(expect(e.target.value));
        }}
        onFocus={() => {
          dispatch(tipbox('expect'));
        }}
      ></textarea>

      <div className='my-1 text-xs text-red-500'>{ErrorMsg.expect}</div>

      {Next === 3 ? <NextBtn /> : null}
      {tipboxName === 'expect' && <TipExpect />}
    </div>
  );
}
