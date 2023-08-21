import { useDispatch, useSelector } from 'react-redux';
import { addtags } from '../../../redux/createSlice/AskSlice';

export default function TagsDropList({ el }) {
  const dispatch = useDispatch();
  const TagsData = useSelector((state) => state.ask.value.tags);

  const addTagList = (tagName) => {
    if (!TagsData.includes(tagName)) {
      dispatch(addtags(tagName));
    } else {
      alert('이미 추가된 태그 입니다.');
    }
  };

  return (
    <li
      className="flex flex-col my-1 mx-2 p-1 w-60 h-32 bg-white hover:bg-sky-100 rounded-md"
      onClick={() => addTagList(el.tagName)}
    >
      <div className="flex justify-between">
        <span className="flex justify-center items-center py-1 px-3 font-semibold text-smm bg-sky-100 rounded-md">
          {el.tagName}
        </span>
        <span className="p-1 text-smm">#{el.tagId}</span>
      </div>
      <div className="mt-1 h-20 truncate whitespace-normal text-xs">
        {el.tagDetail}
      </div>
    </li>
  );
}
