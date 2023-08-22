import { useDispatch, useSelector } from 'react-redux';
import { tags } from '../../redux/createSlice/AskSlice';

export default function TagsList({ el }) {
  const dispatch = useDispatch();
  const TagsData = useSelector((state) => state.ask.value.tagNames);

  const removeTagList = (el) => {
    const newTagsData = TagsData.filter((tag) => tag !== el);
    dispatch(tags(newTagsData));
  };

  return (
    <li className="flex justify-center items-center mx-1 px-1 h-7 text-sm bg-blue-200 rounded-md">
      <span className="ml-1 font-medium">{el}</span>
      <button
        className="mx-1 text-base font-semibold"
        onClick={() => removeTagList(el)}
      >
        &times;
      </button>
    </li>
  );
}
