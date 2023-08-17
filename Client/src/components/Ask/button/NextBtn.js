import { useDispatch } from 'react-redux';
import { next } from '../../../redux/createSlice/TipboxSlice';

export default function NextBtn() {
  const dispatch = useDispatch();

  return (
    <button
      className="w-12 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
      onClick={() => {dispatch(next())}} // redux-toolkit은 Action creator를 자동으로 생성해줌.
    >
      Next
    </button>
  );
}
