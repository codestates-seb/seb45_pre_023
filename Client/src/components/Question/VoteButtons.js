
export default function VoteButtons() {
    return(
        <>
         {/* 투표수 */}
         <div className="flex flex-col items-center">
          <button className="w-[50px] h-[50px] my-2 border-1 rounded-full">
            ▲
          </button>
          <h2 className="text-2xl">0</h2>
          <button className="w-[50px] h-[50px] my-2 border-1 rounded-full">
            ▼
          </button>
          <h2 className="text-2xl">◻</h2>
          <h2 className="text-2xl">Ò</h2>
        </div>
        </>
    );
}
