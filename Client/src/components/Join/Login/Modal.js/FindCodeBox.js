import {
  findcode,
  findmodeentercode,
  findmodeenterpw,
  findmsg,
} from '../../../../redux/createSlice/FindInfoSlice';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';

export default function FindCodeBox() {
  const dispatch = useDispatch();
  const FindInfo = useSelector((state) => state.findinfo);

  const handleCode = () => {
    dispatch(findmsg(''));
    return axios
      .post(
        'http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/auth/email/confirm',
        { email: FindInfo.email, code: FindInfo.code }
      )
      .then((res) => {
        if (res.data.data) {
          dispatch(findmodeentercode(false));
          dispatch(findmodeenterpw(true));
        } else {
          dispatch(findmsg(`Invalid authorization code.`));
        }
      })
      .catch((err) => {
        if (err.response.data.code === 400) {
          dispatch(
            findmsg(
              `Please enter the authorization code. (${err.response.data.code})`
            )
          );
        }
      });
  };

  return (
    <div className="flex flex-col justify-center items-center my-3 w-72 bg-gray-100 rounded-md">
      <span className="mt-4 w-58 text-left text-md font-semibold">code</span>

      <div className="flex justify-between items-center mt-2 mb-4 w-60 h-9">
        <input
          className="w-28 h-9 text-sm text-center border-2 border-solid border-gray rounded-md"
          type="text"
          value={FindInfo.code}
          onChange={(e) => dispatch(findcode(e.target.value))}
        />

        <button
          className="w-28 h-9 bg-sky-500 hover:bg-sky-400 text-sm text-white text-center rounded-md"
          onClick={handleCode}
        >
          Verify
        </button>
      </div>
    </div>
  );
}
