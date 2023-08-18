import {
  findpassword,
  findmodemodal,
  findinit,
} from '../../../../redux/createSlice/FindInfoSlice';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';

export default function FindPassword() {
  const dispatch = useDispatch();
  const FindInfo = useSelector((state) => state.findinfo);

  console.log({ email: FindInfo.email, password: FindInfo.password });

  const handleNewPW = () => {
    return axios
      .patch(
        'http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/auth/password',
        { email: FindInfo.email, password: FindInfo.password }
      )
      .then((res) => {
        dispatch(findmodemodal());
        dispatch(findinit());
      })
      .catch((err) => console.log(err));
  };

  return (
    <div>
      <div className="flex flex-col justify-center items-center">
        <span className="mt-2 w-72 text-left text-md font-semibold">
          New password
        </span>
        <input
          className="my-1 w-72 h-9 pl-2 border-2 border-solid border-gray rounded-md text-sm"
          type="password"
          value={FindInfo.password}
          onChange={(e) => dispatch(findpassword(e.target.value))}
        />
      </div>

      <button
        className="w-72 h-9 mt-2 mb-3 bg-sky-500 hover:bg-sky-400 text-sm text-white text-center rounded-md"
        onClick={handleNewPW}
      >
        Submit new password
      </button>
    </div>
  );
}
