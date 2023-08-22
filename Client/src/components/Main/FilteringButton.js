import { useDispatch } from 'react-redux';
import { filter }from '../../redux/createSlice/QuestionSlice';

export default function FilteringButton() {

  const dispatch = useDispatch();

  return (
    <li className="w-50 flex justify-around rounded-2xl border border-[#E0E0E0]">
      <button
        onClick={()=> dispatch(filter({sort : "CREATED_DATE"}))}
        className="w-[70px] h-[40px]  border-l rounded-2xl border-[#E0E0E0] hover:bg-[#bdbdbd]"
      >
        Newest
      </button>
      <button
        onClick={()=> dispatch(filter({sort : "VIEWS"}))}
        className="w-[70px] h-[40px]  border-l border-[#E0E0E0] hover:bg-[#bdbdbd]"
      >
        Views
      </button>
      <button
        onClick={()=> dispatch(filter({sort : "RECOMMEND"}))}
        className="w-[95px] h-[40px] border-l border-r border-[#E0E0E0] hover:bg-[#bdbdbd]"
      >
        Recommend
      </button>
      <button className="w-[100px] h-[40px] border-r rounded-2xl border-[#E0E0E0] hover:bg-[#bdbdbd]">
        Unanswered
      </button>
    </li>
  );
}
