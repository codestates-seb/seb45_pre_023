import { useDispatch } from 'react-redux';
import { initValue } from '../../../redux/createSlice/AskSlice';

export default function DiscardModal({ handlerDiscardModal }) {
  const dispatch = useDispatch();

  return (
    <div
      className="fixed top-0 left-0 flex flex-col justify-center items-center w-screen h-screen z-20 bg-black/20"
      onClick={handlerDiscardModal}
    >
      <div
        className="flex flex-col justify-start items-start w-120 h-48 bg-white rounded-md"
        onClick={(event) => {
          event.stopPropagation();
        }}
      >
        <div className="flex flex-row justify-between items-start w-104 h-19 ml-7 mt-7">
          <div className="text-2xl text-red-600">Discard question</div>
          <button
            className="font-semibold text-xl text-black"
            onClick={handlerDiscardModal}
          >
            &times;
          </button>
        </div>
        <div className="my-3 mx-4 py-2 w-112 h-10 text-smm text-black">
          Are you sure you want to discard this question? This cannot be undone.
        </div>
        <div className="flex flex-row">
          <button
            className="ml-6 w-32 h-9 text-smm bg-red-500 hover:bg-red-400 text-white rounded-md"
            onClick={() => {
              dispatch(initValue());
              handlerDiscardModal();
            }}
          >
            Discard question
          </button>
          <button
            className="ml-2 w-18 h-9 text-smm bg-white hover:bg-gray-100 text-black rounded-md"
            onClick={handlerDiscardModal}
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
