export default function ReviewdBtn({ isNum, setisNum }) {
  return (
    <button
      className="w-34 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
      onClick={() => {
        setisNum(isNum + 1);
      }}
    >
      Review your question
    </button>
  );
}
