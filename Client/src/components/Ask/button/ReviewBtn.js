import { useDispatch, useSelector } from 'react-redux';
import {
  titleError,
  detailError,
  expectError,
  tagsError,
  initError,
} from '../../../redux/createSlice/AskSlice';
import { next } from '../../../redux/createSlice/TipboxSlice';

export default function ReviewBtn() {
  const AskData = useSelector((state) => state.ask.value);
  const dispatch = useDispatch();

  const handleAskData = () => {
    if (!AskData.title) {
      dispatch(titleError('Please enter the subject.'));
    } else {
      dispatch(titleError(''));
    }
    if (!AskData.detail) {
      dispatch(detailError('Please enter a detailed description.'));
    } else {
      dispatch(detailError(''));
    }
    if (!AskData.expect) {
      dispatch(expectError('Please enter what you expect.'));
    } else {
      dispatch(expectError(''));
    }
    if (!AskData.tags.length) {
      dispatch(tagsError('Please add the relevant tags'));
      return;
    } else {
      dispatch(tagsError(''));
    }
    if (
      AskData.title &&
      AskData.detail &&
      AskData.expect &&
      AskData.tags.length
    ) {
      dispatch(next());
      dispatch(initError());
    }
  };

  return (
    <button
      className="w-34 h-9 mt-5 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
      onClick={handleAskData}
    >
      Review your question
    </button>
  );
}
