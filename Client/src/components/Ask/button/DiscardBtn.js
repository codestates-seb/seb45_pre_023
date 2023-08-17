import { useState } from 'react';
import DiscardModal from '../Modal/DiscardModal';

export default function DiscardBtn() {
  const [isOpen, setisopen] = useState(false);

  const handlerDiscardModal = () => {
    setisopen(!isOpen);
  };

  return (
    <button
      className="relative w-28 h-9 mx-1 bg-gray-100 hover:bg-red-100 rounded-md text-xs text-red-600"
      onClick={handlerDiscardModal}
    >
      Discard draft
      {isOpen && <DiscardModal handlerDiscardModal={handlerDiscardModal} />}
    </button>
  );
}
