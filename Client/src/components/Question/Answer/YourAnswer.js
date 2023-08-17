
export default function YourAnswer() {
    return (
        <>
         {/* Answer 입력 창 */}
         <div className="mt-8 border-t-2">
            <h1 className="text-3xl my-6">Your Answer</h1>
            <input
              className="w-[1000px] h-[250px] mt-4 border-2 border-gray-300 rounded-md"
              type="text"
              placeholder="내용 입력"
            />
            <button className="w-[150px] h-[50px] mt-12 mb-12 rounded-2xl bg-[#2196F3] text-[white]">
              Reply Answer
            </button>
          </div>
        </>
    )
}