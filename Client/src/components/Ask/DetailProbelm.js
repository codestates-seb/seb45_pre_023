import NextBtn from './button/NextBtn';
import TipDetail from './Tips/TipDetail';
import { useSelector, useDispatch } from 'react-redux';
import { detail } from '../../redux/createSlice/AskSlice';
import { tipbox } from '../../redux/createSlice/TipboxSlice';

export default function DetailProbelm() {
  const dispatch = useDispatch();
  const Next = useSelector((state) => state.tipbox.position);
  const tipboxName = useSelector((state) => state.tipbox.tipboxName);
  const InputValue = useSelector((state) => state.ask.value);
  const ErrorMsg = useSelector((state) => state.ask.errmsg);

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">What are the details of your problem?</div>

      <div className="my-1 text-xs">
        Introduce the problem and expand on what you put in the title. Minimum
        20 characters.
      </div>

      <textarea
        className="my-1 py-1.5 pl-2 h-60 text-sm bg-white border-2 border-solid border-gray rounded-md"
        value={InputValue.detail}
        onChange={(e) => {
          dispatch(detail(e.target.value));
        }}
        onFocus={() => {
          dispatch(tipbox('detail'));
        }}
      ></textarea>

      <div className='my-1 text-xs text-red-500'>{ErrorMsg.detail}</div>

      {Next === 2 && <NextBtn />} 
      {tipboxName === 'detail' && <TipDetail />}
    </div>
  );
}
