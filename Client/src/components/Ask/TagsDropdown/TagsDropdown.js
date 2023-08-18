import { useSelector } from 'react-redux';
import TagsDropList from './TagsDropList';
import { dummydata } from './dummy';

export default function TagsDropdown() {
  const TagsData = useSelector((state) => state.ask.tagsdata.tagslist);

  return (
    <ul className="absolute grid grid-cols-3 top-28 py-1 w-[796px] h-56 z-10 overflow-y-auto bg-white border-2 border-solid border-gray rounded-md">
      {dummydata.map((el, idx) => (
        <TagsDropList key={idx} el={el} />
      ))}
    </ul>
  );
}
