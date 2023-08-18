import NextBtn from './button/NextBtn';
import TipTags from './Tips/TipTags';
import { useSelector, useDispatch } from 'react-redux';
import { addtags } from '../../redux/createSlice/AskSlice';
import { tipbox } from '../../redux/createSlice/TipboxSlice';
import TagsDropdown from './TagsDropdown/TagsDropdown';
import { setTagMode } from '../../redux/createSlice/AskSlice';
import TagsList from './TagsList';

export default function Tags() {
  const dispatch = useDispatch();
  const Next = useSelector((state) => state.tipbox.nextbtn);
  const tipboxName = useSelector((state) => state.tipbox.tipboxName);
  const ErrorMsg = useSelector((state) => state.ask.errmsg);
  const TagsData = useSelector((state) => state.ask.value.tags);
  const TagsMode = useSelector((state) => state.ask.tagsdata.mode);

  const addTagList = (e) => {
    if (e.key === 'Enter') {
      if (e.target.value && !TagsData.includes(e.target.value)) {
        dispatch(addtags(e.target.value));
        e.target.value = '';
      }
    }
  };

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className={`font-semibold ${Next < 4 && 'text-gray-300'}`}>Tags</div>

      <div className={`my-1 text-xs ${Next < 4 && 'text-gray-300'}`}>
        Add up to 5 tags to describe what your question is about. Start typing
        to see suggestions.
      </div>

      <div className="flex justify-start items-center my-1 pl-1 h-11 bg-white border-2 border-solid border-gray rounded-md">
        <ul className="flex flex-row justify-start items-center p-0">
          {TagsData.map((el, idx) => (
            <TagsList key={idx} el={el} />
          ))}
        </ul>
        <input
          className="py-1.5 pl-2 text-sm w-full"
          onKeyUp={(e) => addTagList(e)}
          placeholder="e.g. (mysql, json, typescript)"
          onFocus={() => {
            dispatch(tipbox('tags'));
            dispatch(setTagMode(true));
          }}
          onBlur={() => {
            dispatch(setTagMode(false));
          }}
          disabled={Next < 4}
        />
      </div>

      {TagsMode && <TagsDropdown />}

      <div className="my-1 text-xs text-red-500">{ErrorMsg.tags}</div>

      {Next === 4 ? <NextBtn /> : null}
      {tipboxName === 'tags' && <TipTags />}
    </div>
  );
}
