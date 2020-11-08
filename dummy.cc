#include <iostream>
#include <ctime>
#include <chrono>
#include <thread>

int main(int argc, char** argv) {
    while(true) {
        std::string currentTime = std::to_string(std::time(0));
        std::cout << "Dummy's heart is beating: " << currentTime << std::endl;
        std::this_thread::sleep_for(std::chrono::seconds(1));
    }
    return 0;
}