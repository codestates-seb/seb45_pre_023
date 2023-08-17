import NextBtn from './button/NextBtn';
import TipTags from './Tips/TipTags';
import { useSelector, useDispatch } from 'react-redux';
import { tags } from '../../redux/createSlice/AskSlice';
import { tipbox } from '../../redux/createSlice/TipboxSlice';

export default function Tags() {
  const dispatch = useDispatch();
  const Next = useSelector((state) => state.tipbox.position);
  const tipboxName = useSelector((state) => state.tipbox.tipboxName);
  const InputValue = useSelector((state) => state.ask.value);
  const ErrorMsg = useSelector((state) => state.ask.errmsg);

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">Tags</div>

      <div className="my-1 text-xs">
        Add up to 5 tags to describe what your question is about. Start typing
        to see suggestions.
      </div>

      <input
        className="mt-1 mb-3 py-1.5 pl-2 text-sm bg-white border-2 border-solid border-gray rounded-md"
        value={InputValue.tags}
        onChange={(e) => {
          dispatch(tags(e.target.value));
        }}
        placeholder="e.g. (mysql json typescript)"
        onFocus={() => {
          dispatch(tipbox('tags'));
        }}
      ></input>

      <div className='my-1 text-xs text-red-500'>{ErrorMsg.tags}</div>

      {Next === 4 ? <NextBtn /> : null}
      {tipboxName === 'tags' && <TipTags />}
    </div>
  );
}
