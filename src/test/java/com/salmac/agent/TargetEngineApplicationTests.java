package com.salmac.agent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TargetEngineApplicationTests {

	@Test
	void contextLoads() {
		int[] arr = {-4,3,5,1,6,7};
		System.out.println(solution(arr));
		
	}

	public int solution(int[] A) {
		sortArra(A);
		boolean hasPositive = false;
		for(int i = 0; i < A.length; i++){
			if(A[i]> 1) {
				hasPositive = true;
				if(i != 0 && A[i-1] != A[i]-1) {
					return A[i] -1;
				}
			}
		}
		if(hasPositive) {
			return A[A.length-1]+1;
		}
		return 1;
	}

	public void sortArra(int[] A){
        int len  = A.length;
        for(int i = 0; i < len; i++){
            for(int j = i+1; j <len; j++){
            	if(A[i] > A[j]) {
            		int tmp = A[i];
            		A[i] = A[j];
            		A[j] = tmp;
            	}
            }
        }
    }

}
