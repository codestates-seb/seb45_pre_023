import useAskBox from '../../../hooks/useAskBox';

export default function NextBtn() {
  const {up} = useAskBox();

  return (
    <button
      className="w-12 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
      onClick={up}
    >
      Next
    </button>
  );
}